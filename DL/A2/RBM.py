class BB_RBM(nn.Module):

    def __init__(self, n_vis, n_hid, k):
        super(BB_RBM, self).__init__()
        self.v_bias = nn.Parameter(torch.zeros(1, n_vis))
        self.h_bias = nn.Parameter(torch.zeros(1, n_hid))
        self.weights = nn.Parameter(torch.randn(n_hid, n_vis))
        self.k = k

    def sample_hidden(self, v_prob):
        h_sig = torch.sigmoid(F.linear(v_prob, self.weights, self.h_bias)) # xA.T + b -> linear func
        return h_sig.bernoulli()

    def sample_visible(self, h_prob):
        v_sig = torch.sigmoid(F.linear(h_prob, self.weights.t(), self.v_bias))
        return v_sig.bernoulli()

    def free_energy(self, v_vals):
        v_term = torch.matmul(v_vals, self.v_bias.t())
        w_v_b = F.linear(v_vals, self.weights, self.h_bias)
        h_term = torch.sum(F.softplus(w_v_b), dim=1)
        return torch.mean(-h_term - v_term)
    
    def free_energy_gap(self, v_vals, v_gibbs):
        return self.free_energy(v_vals) - self.free_energy(v_gibbs)
    
    def k_step_contrasive_divergence(self, h_vals):
        for _ in range(self.k):
            v_gibbs = self.sample_visible(h_vals)
            h_vals = self.sample_hidden(v_gibbs)
        return v_gibbs, h_vals

    def forward(self, v_vals):
        h_vals = self.sample_hidden(v_vals)
        return v_vals, self.k_step_contrasive_divergence(h_vals)
    
class GB_RBM(BB_RBM):
    # Only changes are with sampling visable and free energy
    def sample_visible(self, h_prob):
        h_lin = F.linear(h_prob, self.weights.t(), self.v_bias)
        return h_lin
        
    def free_energy(self, v_vals):
        w_v_b = F.linear(v_vals, self.weights, self.h_bias)
        v_term = 0.5*torch.sum(torch.square(v_vals-self.v_bias),dim=1)
        h_term = torch.sum(F.softplus(w_v_b), dim=1)
        return torch.mean(-h_term + v_term)