package ebank.core;

import ebank.core.model.domain.GwPayments;

public interface EsbService {
	public void send(GwPayments payment);

}
