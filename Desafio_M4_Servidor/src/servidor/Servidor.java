package servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ftp.Requisicao;
import ftp.Resposta;


public class Servidor {
	public static void main (String args[]) {
		
		ServerSocket serverSocket;
		Socket cliente;
		Resposta resposta;
		
		//realiza a tentiva de incialização do servidor, caso não ocorra, acontece um tratamento 
		try {
			serverSocket = new ServerSocket(14500); //porta definida aleatoriamente
			System.out.println("Servidor iniciado na porta 14500");
			
			while(true) {
				cliente = serverSocket.accept();
				ObjectInputStream is = new ObjectInputStream(cliente.getInputStream());
				ObjectOutputStream os = new ObjectOutputStream(cliente.getOutputStream());
				
				//realiza a leitura da requisicao do cliente
				Requisicao req = (Requisicao)is.readObject();
				
				System.out.println("---------------\nRequisição do arquivo de nome: " + req.getConteudoMensagem());
				
				//se o tipo do que foi enviado é uma requisação para verificar o nome do arquivo então ele verifica se o arquivo existe ou nao
				if (req.getTipoMensagem() == Requisicao.NOMEARQUIVO_REQUISICAO) {
				
					File arquivo = new File(req.getConteudoMensagem());
					resposta = new Resposta();
					
					//caso o arquivo exista, sinaliza essa situação e realiza a leitura do conteudo do mesmo
					if (arquivo.exists()) {
						resposta.setCodigoResposta(Resposta.ARQUIVO_EXISTE);
						System.out.println("Arquivo encontrado.");
						os.writeObject(resposta);
						
						FileReader fr = new FileReader(arquivo);
						BufferedReader br = new BufferedReader(fr);
						String linha = null;
						do {
							
							req = (Requisicao)is.readObject();
							if (req.getTipoMensagem() == Requisicao.CONTEUDOARQUIVO_REQUISICAO) {
								linha =  br.readLine();
								resposta = new Resposta();
								if(linha != null) {
									resposta.setCodigoResposta(Resposta.CONTEUDO_DISPONIVEL);
									resposta.setConteudoResposta(linha);
								}else {
									resposta.setCodigoResposta(Resposta.FINAL_DO_ARQUIVO);
									System.out.println("Final do arquivo enviado"); //sinaliza o termino da leitura do arquivo
								}	
								os.writeObject(resposta);
							}

						}while (linha != null);
						fr.close();
					//caso o arquivo não exista, envia a resposta avisando de tal ocorrido
					}else {
						resposta.setCodigoResposta(Resposta.ARQUIVO_NAO_ENCONTRADO);
						System.out.println("Arquivo inexistente.");
						os.writeObject(resposta);
					}	
				
				}	
				
				is.close();
				os.close();
				cliente.close();
			}
			
		}
		//mensagem caso o servidor não inicie como esperado
		catch(Exception ex) {
			System.err.println("Problema encontrado no Servidor");
			ex.printStackTrace();	
		}
		
	}

}
