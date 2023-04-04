package ftp;

import java.io.Serializable;

public class Requisicao implements Serializable {
	
	
	//clase criada com o intuito de efetuar o controle das requisições efetuada pelo cliente, conseguindo mediar o tipo e conteudo
	
	private int	tipoMensagem;
	private String conteudoMensagem;
	public static final int NOMEARQUIVO_REQUISICAO = 0;
	public static final int CONTEUDOARQUIVO_REQUISICAO = 1;
	
	public String getConteudoMensagem() {
		return conteudoMensagem;
	}
	public void setConteudoMensagem(String conteudoMensagem) {
		this.conteudoMensagem = conteudoMensagem;
	}
	
	public int getTipoMensagem() {
		return tipoMensagem;
	}
	
	public void setTipoMensagem(int tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

}
