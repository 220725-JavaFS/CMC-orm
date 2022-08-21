package com.revature.models;

import java.util.Objects;

public class Actor {
	private int actor_id;
	private String actor_name;

	public Actor() {
		super();
	}

	public Actor(int actor_id, String actor_name) {
		super();
		this.actor_id = actor_id;
		this.actor_name = actor_name;
	}

	public int getActor_id() {
		return actor_id;
	}

	public void setActor_id(int actor_id) {
		this.actor_id = actor_id;
	}

	public String getActor_name() {
		return actor_name;
	}

	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actor_id, actor_name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		return actor_id == other.actor_id && Objects.equals(actor_name, other.actor_name);
	}

	@Override
	public String toString() {
		return "Actor [actor_id=" + actor_id + ", actor_name=" + actor_name + "]";
	}

}
