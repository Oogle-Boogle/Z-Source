package com.zamron.world.content.scratchcard;


public class ScratchCardCombination {

	private ScratchCardInstanced first, second, third;

	public ScratchCardCombination(ScratchCardInstanced first, ScratchCardInstanced second, ScratchCardInstanced third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public ScratchCardInstanced getFirst() {
		return first;
	}

	public ScratchCardInstanced getSecond() {
		return second;
	}

	public ScratchCardInstanced getThird() {
		return third;
	}
}