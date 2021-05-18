package me.eccentric_nz.TARDIS.mobfarming;

import java.util.List;

public class TARDISPetsAndFollowers {

	private final List<TARDISPet> pets;
	private final List<TARDISFollower> followers;

	public TARDISPetsAndFollowers(List<TARDISPet> pets, List<TARDISFollower> followers) {
		this.pets = pets;
		this.followers = followers;
	}

	public List<TARDISPet> getPets() {
		return pets;
	}

	public List<TARDISFollower> getFollowers() {
		return followers;
	}
}
