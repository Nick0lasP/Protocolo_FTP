package ftp;

import java.io.Serializable;

public class Resposta implements Serializable {

	//clase criada com o intuito de efetuar o controle das respostas dadas pelo servidor, conseguindo mediar a busca de arquivos bem como seu conteudo
	
	private int codigoResposta;
	private String conteudoResposta;
	public static final int ARQUIVO_EXISTE = 1;
	public static final int ARQUIVO_NAO_ENCONTRADO = 2;
	public static final int CONTEUDO_DISPONIVEL = 3;
	public static final int FINAL_DO_ARQUIVO = 4;
	
	public String getConteudoResposta() {
		return conteudoResposta;
	}
	
	public void setConteudoResposta(String conteudoResposta) {
		this.conteudoResposta = conteudoResposta;
	}
	
	public int getCodigoResposta() {
		return codigoResposta;
	}
	
	public void setCodigoResposta(int codigoResposta) {
		this.codigoResposta = codigoResposta;
	}
	
}
