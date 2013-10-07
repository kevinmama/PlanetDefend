package starsector.mod.pld.event;

import starsector.mod.nf.event.BaseEvent;

public class SalaryEvent extends BaseEvent{

	/**
	 * how much you get
	 */
	private int salary;
	
	/**
	 * how much you pay
	 */
	private int pay;
	
	/**
	 * true if you can't pay enough to your man
	 */
	private boolean indebt;
	

	public SalaryEvent(int salary, int pay, boolean indebt) {
		super(PLDEventType.SALARY);
		this.salary = salary;
		this.pay = pay;
		this.indebt = indebt;
	}

	public int getSalary() {
		return salary;
	}

	public int getPay() {
		return pay;
	}

	public boolean isIndebt() {
		return indebt;
	}
	
}
