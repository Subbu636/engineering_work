from gym.envs.registration import register

register(
    'chakra-v1',
    entry_point='gym_chakra.envs:chakra',
    max_episode_steps=40, # 
)