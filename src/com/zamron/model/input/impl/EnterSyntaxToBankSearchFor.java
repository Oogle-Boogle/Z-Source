package com.zamron.model.input.impl;

import com.zamron.model.container.impl.Bank.BankSearchAttributes;
import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		if(searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
