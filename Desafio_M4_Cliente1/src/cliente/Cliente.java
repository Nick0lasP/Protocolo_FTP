package cliente;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ftp.Requisicao;
import ftp.Resposta;

public class Cliente {
	
	public static void main (String args[]) {
		Socket socket;
		Scanner sc = new Scanner(System.in);
		
		//realiza a tentiva de conexão do cliente, caso não ocorra, acontece um tratamento ao final
		try {
			socket = new Socket("localhost", 14500);//nome do host e porta
			ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			//o listar arquivos utiliza um caminho relativo ao diretorio do meu computador, caso queira testar, deverá alteralo
			System.out.println("Olá!\nEssa é a lista de arquivos disponíves no momento:"+ listarArquivosDoServidor("D:\\Eclipse Java\\Eclipse-Workspace\\Desafio_M4_Servidor"));
			//usuario digita o nome do arquivo que quer, baseado na lista disponível
			System.out.println("---------------\nDigite o arquivo que deseja:");
			String nomeArquivo = sc.nextLine();
			Requisicao req = new Requisicao();
			req.setTipoMensagem(Requisicao.NOMEARQUIVO_REQUISICAO);
			req.setConteudoMensagem(nomeArquivo);
			
			//envia a requisição para verificar se o arquivo existe
			os.writeObject(req);
			
			//analisa para ver se a resposta diz que o arquivo existe ou não
			Resposta resposta = (Resposta)is.readObject();
			if(resposta.getCodigoResposta() == Resposta.ARQUIVO_EXISTE) {
				System.out.println("Arquivo existe no servidor... INICIANDO DOWNLOAD!");
				FileWriter fw = new FileWriter(new File(nomeArquivo));
				
				//caso o arquivo exista, faz o download de seu conteudo
				do {
					req = new Requisicao();
					req.setTipoMensagem(Requisicao.CONTEUDOARQUIVO_REQUISICAO);
					
					os.writeObject(req);
					
					resposta = (Resposta)is.readObject();
					if (resposta.getCodigoResposta() == Resposta.CONTEUDO_DISPONIVEL) {
						fw.write(resposta.getConteudoResposta()+ "\n");
					}
					
				}while (resposta.getCodigoResposta() != Resposta.FINAL_DO_ARQUIVO); //chegou no final do arquivo
				System.out.println("DOWNLOAD CONCLUÍDO!");
				fw.close();
			}
			//caso o arquivo não exista
			else if (resposta.getCodigoResposta() == Resposta.ARQUIVO_NAO_ENCONTRADO)
				System.out.println("Arquivo não encontrado no servidor :c");
			
			is.close();
			os.close();
			socket.close();
			
		}
		//mensagem caso o cliente não consiga se conectar por algum problema
		catch(Exception ex) {
			System.err.println("Problema encontrado no Cliente");
			ex.printStackTrace();	
		}
		
	}
	
	//metodo estatico que lista os arquivos disponíveis para download do servidor
	public static Set<String> listarArquivosDoServidor(String local) throws IOException {
	    try (Stream<Path> stream = Files.list(Paths.get(local))) {
	        return stream
	          .filter(arquivo -> !Files.isDirectory(arquivo))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}		
	
}


