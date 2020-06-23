#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <unistd.h>
#include <netinet/in.h>
#include <netdb.h>
#include "proxy_parse.h"


// void writesocket(char *buf,int sock_fd,int rec_fd, int *l)
// {

// 	int char_sent = 0
// 	while(char)
// }
int main(int argc, char * argv[])
{
	if(argc != 2)
	{
		perror("Input Error");
	}
	socklen_t len;
	char* port_id = argv[1];
	//printf("hey");
   	struct addrinfo paddr, *temp_addr;
   	memset(&paddr, 0, sizeof(paddr));
    paddr.ai_family = AF_UNSPEC;
    paddr.ai_socktype = SOCK_STREAM;
    paddr.ai_flags = AI_PASSIVE;

    //printf("hi");
    // Error handling
    if (getaddrinfo(NULL, port_id, &paddr, &temp_addr) < 0) 
    {
      perror("GETADDR error");
      exit(EXIT_FAILURE);
  	}

  	// Creating Socket for client reqs.
  	int sock_fd = socket(temp_addr->ai_family,temp_addr->ai_socktype,temp_addr->ai_protocol);
  	int bind_var = bind(sock_fd,temp_addr->ai_addr,temp_addr->ai_addrlen);
  	int l_var = listen(sock_fd, 50);
  	if(sock_fd < 0 || bind_var < 0 || l_var < 0)
  	{
  		perror("Socket Error");
  		exit(EXIT_FAILURE);
  	}

  	// Client Reqs.

  	int num_clients = 0,client_fd,num_cl = 0;
  	struct sockaddr client;
  	len = sizeof(struct sockaddr);

  	printf("Proxy Server Started\n");

  	while(1)
  	{
  		client_fd = accept(sock_fd, &client, &len);
  		if(client_fd <= 0)
  		{
  			perror("Client Error");
  			shutdown(client_fd, SHUT_RDWR);
  			close(client_fd);
  			continue;
  		}
  		num_cl++;
  		// printf("Client : %d \n",num_cl);

  		// Requests

  		int pid = fork();
  		if(pid < 0 )
  		{
  			perror("Fork error");
			shutdown(client_fd, SHUT_RDWR);
			close(client_fd);
			continue;
  		}
  		if(pid == 0)
  		{
  			// close(sock_fd)

  				//printf("In child\n");
  			// read client request
  				printf("Client : %d \n",num_cl);
	  			int BUF_SIZE = 4097;
	  			char *Ierr="Not Implemented error\n";
	  			char *perr="Parse Error\n";
	  			char *rerr="Recieve Error\n";

	  			//char* req= (char *) malloc(BUF_SIZE);
	  			char* re = (char *) malloc(BUF_SIZE);
	  			//strcpy(re,req);
	  			//printf("After malloc %s\n",re );
	  			//request error
	  			if(re == NULL)
	  			{	
	  				// send(client_fd,  ,strlen(err),0);
	  				shutdown(client_fd, SHUT_RDWR);
					close(client_fd);
					exit(EXIT_FAILURE);
	  			}
	  			char buff[BUF_SIZE];
	  			int req_capacity = BUF_SIZE;
	  			int req_size = 0;
	  			//printf("Request BC\n");
	  			// while(strstr (req,"\r\n\r\n") == NULL)
	  			int n = 0;
	  			strcpy(re,"");
	  			//printf("Before \n %s\n",re);
	  			while(strstr (re,"\r\n") == NULL)
	  			{
	  				n++;
	  				//printf("n = %d \n",n);
	  				int rec = recv(client_fd,buff,BUF_SIZE,0);
	  				//printf("Recieved bytes %s\n",buff);
	  				if(rec < 0)
	  				{
	  					printf("recv err\n");
	  					send(client_fd, rerr ,strlen(rerr),0);
		  				shutdown(client_fd, SHUT_RDWR);
						close(client_fd);
						exit(EXIT_FAILURE);
	  				}
	  				if(rec==0) break;
	  				buff[rec]='\0';
	  				req_size+=rec;
	  				if(	req_size > req_capacity)
	  				{
	  						//if capacity exceeded
	  						req_capacity*=2;
	  						re = (char*) realloc(re,req_capacity+1);
	  						//realloc error
	  						if(re == NULL)
				  			{	
				  				printf("realloc err\n");
				  				send(client_fd, rerr ,strlen(rerr),0);
				  				shutdown(client_fd, SHUT_RDWR);
								close(client_fd);
								exit(EXIT_FAILURE);
				  			}
	  				}
	  				strcat(re,buff);
	  			}
	  			////Parsing the request
	  			//printf("Request is %s\n",re);

	  			struct ParsedRequest *preq;
	  			preq = ParsedRequest_create();
	  			//handling parse error
	  			// char perr[]="Parse error ";
	  			strcat(re,"\r\n");
	  			int Rerr = ParsedRequest_parse(preq , re, strlen(re));
	  			printf("%d\n", Rerr);
	  			if( Rerr== -1)
	  			{
	  				printf("parse err\n");
	  				send(client_fd, perr ,strlen(perr),0);
	  				shutdown(client_fd, SHUT_RDWR);
					close(client_fd);
					exit(EXIT_FAILURE);
	  			}
	  			if(Rerr== -2)
	  			{
	  				printf("Not Implementation err\n");
	  				send(client_fd, Ierr ,strlen(Ierr),0);
	  				shutdown(client_fd, SHUT_RDWR);
					close(client_fd);
					exit(EXIT_FAILURE);
	  			}
	  			if (preq->port == NULL )
	  			{
	  					//"default" port allocation
	  					preq->port = (char*) "80";
	  			}
	  			//printf("Server Request CREATING\n");
	  			///Creating server request
	  			int headers_span = ParsedHeader_headersLen(preq);
				char *headers = (char *) malloc(headers_span + 4);

				
				ParsedRequest_unparse_headers(preq,headers,headers_span);
				//printf("datindi\n");
				headers[headers_span]='\0';

				int serv_req_len = strlen(preq->method)+strlen(preq->path)+strlen(preq->version)+headers_span+80;
				//printf("idi datindi\n");
				char *serv_req = (char *) malloc(serv_req_len + 1);
				strcpy(serv_req, preq->method);
				strcat(serv_req, " ");
				strcat(serv_req, preq->path);
				strcat(serv_req, " ");
				strcat(serv_req, preq->version);
				strcat(serv_req, "\r\n");
				char *t =(char *) malloc(15);
				strcpy(t,"HOST: ");
				strcat(t,preq->host);
				strcat(serv_req,t);
				strcat(serv_req, "\r\n");

				strcat(serv_req,"Connection: close");
				strcat(serv_req, "\r\n");

				strcat(serv_req, headers);

				socklen_t len;

				///Creating socket to Server
				//printf( "Sending req to server\n");
			   	struct addrinfo saddr, *temp1_addr;
			   	memset(&saddr, 0, sizeof(saddr));
			    saddr.ai_family = AF_UNSPEC;
			    saddr.ai_socktype = SOCK_STREAM;
			    // paddr.ai_flags = AI_PASSIVE;


			    // Error handling
			    if (getaddrinfo(preq->host,preq->port , &saddr, &temp1_addr) < 0) 
			    {
			      perror("GETADDR error");
			      exit(EXIT_FAILURE);
			  	}

			  	// Creating Socket for client reqs.

			  	int server_fd = socket(temp1_addr->ai_family,temp1_addr->ai_socktype,temp1_addr->ai_protocol);
			  	int connect1_var = connect(server_fd,temp1_addr->ai_addr,temp1_addr->ai_addrlen);
			  	if(server_fd < 0 || connect1_var < 0 )
			  	{
			  		perror("Server Socket Error");
			  		exit(EXIT_FAILURE);
			  	}
			  	//Send request to server
			  	//printf("Server Request sent\n");
			  	send(server_fd,serv_req,strlen(serv_req),0);

			  	// printf("Server Request sent\n");
			  	//Send reply from server to client
			  	sleep(1);
			  	char *r_buff = (char *) malloc(1024);
			  	int rec_error;
			  	int x = 0;
			  	int lent = 409600;
			  	char *r_total = (char *) malloc(lent);
			  	strcpy(r_total,"");
			  	while((rec_error = recv(server_fd,r_total,lent,0)) > 0)
			  	{
			  	// while( (rec_error = recv(server_fd,r_buff,1024,0)) > 0)
			  	// {
			  	// 	sleep(1);
			  	// 	strcat(r_total,r_buff);
			  	// 	send(client_fd,r_buff,strlen(r_buff),0);
			  	x++;
			  	// 	strcpy(r_buff,"");
			  		
			  	// }
			  	// sleep(1);
			  	send(client_fd,r_total,lent,0);
			  	strcpy(r_total,"");
			  }
			  	printf("Client : %d Job done in %d \n",num_cl, x);
	  			char serr[]="Server recieve error";
			  	if(rec_error < 0)
			  	{
			  		send(client_fd,serr,strlen(serr),0);
			  		shutdown(client_fd, SHUT_RDWR);
					shutdown(server_fd, SHUT_RDWR);
					 close(client_fd);
					 close(server_fd);
					 exit(EXIT_FAILURE);
			  	}

			  	ParsedRequest_destroy(preq);

				shutdown(client_fd, SHUT_RDWR);
				shutdown(server_fd, SHUT_RDWR);
				close(client_fd);
				close(server_fd);

  		}



  		num_clients++;
  		while(waitpid(-1,NULL,WNOHANG) >0)
  		{
  			num_clients--;
  		}
  		if(num_clients >= 20)
  		{
  			wait(NULL);
  			num_clients--;
  		}
  		close(client_fd);

  	}
  	shutdown(sock_fd, SHUT_RDWR);
	  close(sock_fd);

	  return 0;
}