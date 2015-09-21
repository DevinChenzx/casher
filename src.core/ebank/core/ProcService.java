package ebank.core;

import ebank.core.model.domain.GwProc;

public interface ProcService {
	public void tx_saveProc(GwProc proc);
	/**
	 * update the procedure states and operation result
	 * @param proc
	 * @return
	 */
	public int tx_updateProcSts(GwProc proc);
	
	public GwProc getProcess(String procname,String proctxid);

}
