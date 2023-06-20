package com.vogella.maven.quickstart;

import org.jibble.pircbot.PircBot;

public class MyBotMain extends PircBot{
	public static void main(String[] args) throws Exception{
		MyBot bot = new MyBot();
		bot.setVerbose(true);
		bot.connect("irc.joseon.kr");
		bot.joinChannel("#pircbot");
		bot.sendMessage("#pircbot", "Hi, welcome to pircbot! here you will be able to request weather in most cities around the world and convert currencies. \n");
		bot.sendMessage("#pircbot", "To find the weather simply type \"weather in *city/zip*\".\n");
		bot.sendMessage("#pircbot", "To convert currency the message must be in the form \"convert *amount* *currency* to *other currency*.\" Ex. \"convert 10 USD to MXN\"");
	}

}
