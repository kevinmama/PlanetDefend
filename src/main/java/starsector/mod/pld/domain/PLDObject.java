package starsector.mod.pld.domain;

import starsector.mod.pld.PLD;



/**
 * Object should put to PLDRegistry
 * @author fengyuan
 *
 */
public abstract class PLDObject {
	
	/**
	 * object's global id
	 */
	private long gid;
	
	/**
	 * get the unique object id. 
	 * gid is set when it is registered to PlDregistry
	 * @return
	 */
	public long getGid() {
		return gid;
	}
	
	/**
	 * provide interface for PLDRegistry to set the Gid
	 * @param gid
	 */
	void setGid(long gid){
		this.gid = gid;
	}
	
	protected abstract String getName();
	
	/**
	 * get PLD object's qualified name
	 * @return
	 */
	public String getQualifiedName(){
		return getName() + "#" + getGid();
	}
	
	// rename ?
	
	/**
	 * dispose the object and remove it from registry.
	 * call by PLDRegistry
	 */
	public void dispose(){
		PLD.getFactory().dispose(this);
	}
	
}
