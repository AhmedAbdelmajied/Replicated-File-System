package interfaces;

import java.rmi.RemoteException;

import servers.Transaction;

public interface ReplicaToMasterInterface {

	public int abort(long TxnID) throws RemoteException;

	public Transaction newTxn(long TxnID) throws RemoteException;

	public String getPath() throws RemoteException;
}
