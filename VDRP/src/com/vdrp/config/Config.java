package com.vdrp.config;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import com.vdrp.client.ElGamal;



public class Config {
	public ElGamal eg;
	public Config(String filename) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException {

		String privateKeyFile = "Config//privateKey";
		eg = new ElGamal(privateKeyFile);
	}

	public Config() throws IOException, ClassNotFoundException {

		eg = new ElGamal();

	}

}
