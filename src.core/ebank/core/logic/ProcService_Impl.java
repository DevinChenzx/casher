package ebank.core.logic;

import ebank.core.ProcService;
import ebank.core.model.dao.GwProcDAO;
import ebank.core.model.domain.GwProc;

public class ProcService_Impl implements ProcService{

	private GwProcDAO procDAO;
	public void tx_saveProc(GwProc proc) {
		procDAO.save(proc);
	}
	
	public GwProc getProcess(String procname, String proctxid) {		
		return procDAO.getProcess(procname, proctxid);
	}

	public int tx_updateProcSts(GwProc proc){
		return procDAO.updateProcSts(proc);
	}
	public void setProcDAO(GwProcDAO procDAO) {
		this.procDAO = procDAO;
	}
	
	

}
