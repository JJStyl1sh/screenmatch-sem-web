package br.com.usacucar.screenmatch.service;


public interface IConverteDados {
   <T> T  obterDados(String json, Class<T> classe);
}