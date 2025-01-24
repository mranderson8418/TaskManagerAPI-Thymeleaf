package com.taskmanager.controllers;

public class LastWord {

	private String str;

	private String[] words;

	public LastWord() {
		this.str = "";
		this.words = null;
	}

	public LastWord(String str) {

		this.str = str.toUpperCase();

	}

	public String getLastWord() {
		String[] words = str.split("\\.");

		if (words.length > 0) {

			String lastWord = words[words.length - 1];
			return lastWord;
		} else {

			return "No period found in the String";
		}
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String[] getWords() {
		return words;
	}

	public void setWords(String[] words) {
		this.words = words;
	}

}
