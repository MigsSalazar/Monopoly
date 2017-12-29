package main.java.models;

import com.google.gson.annotations.Expose;

public class Railroad extends Property {
	
	@Expose private GlobalCounter gcount;
	
	public Railroad(String n, String o, int p, int pr, boolean mb, GlobalCounter gc) {
		super(n, o, p, pr, mb);
		// TODO Auto-generated constructor stub
		gcount = gc;
	}
	
	@Override
	public void setOwner(String o){
		if(owner == null || !owner.equals("")){
			owner = o;
			gcount.incCount();
		}else{
			owner = o;
		}
	}

	@Override
	public int getRent() {
		// TODO Auto-generated method stub
		switch(gcount.getCount()){
			case 1: return 25;
			case 2: return 50;
			case 3:	return 100;
			case 4: return 200;
			default: return 0;
		}
	}

	public GlobalCounter getGcount() {
		return gcount;
	}

	public void setGcount(GlobalCounter gcount) {
		this.gcount = gcount;
	}


}
