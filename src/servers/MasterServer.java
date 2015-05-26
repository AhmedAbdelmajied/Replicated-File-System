package servers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import interfaces.MasterServerInterface;
import interfaces.ReplicaServerInterface;

public class MasterServer implements MasterServerInterface {

	private String dir;
	private String ip;
	private int port;

	private HashMap<String, String[]> metaData;
	// private ArrayList<ReplicaServerInterface> replicaServers;
	private ArrayList<String> replicasIp;
	private HashMap<Long, Transaction> transactions;

	private Random random;

	public MasterServer(String ip, int port, String dir) {
		this.ip = ip;
		this.port = port;
		this.dir = dir;

		metaData = new HashMap<String, String[]>();
		replicasIp = new ArrayList<String>();
		transactions = new HashMap<Long, Transaction>();

		random = new Random();
		init(port);

	}

	private void init(int port) {
		// open connection to clients

		// Read Replica Servers information
		try {
			InputStreamReader converter = new InputStreamReader(
					new FileInputStream(new File("repServers.txt")));
			BufferedReader in = new BufferedReader(converter);
			String line = null;

			in.readLine();

			while ((line = in.readLine()) != null) {
				String[] parsed = line.split(",");
				replicasIp.add(parsed[0]);
				// Registry registry = LocateRegistry.getRegistry(parsed[0],
				// Integer.parseInt(parsed[1]));
				// replicaServers.add((ReplicaServerInterface) registry
				// .lookup(parsed[2]));
			}

			converter = new InputStreamReader(new FileInputStream(new File(
					"Metadata.txt")));
			in = new BufferedReader(converter);
			line = null;

			in.readLine();

			while ((line = in.readLine()) != null) {
				String[] parsed = line.split(",");
				String[] replicas = { parsed[1], parsed[2], parsed[3] };
				metaData.put(parsed[0], replicas);

			}
			in.close();
		} catch (IOException e) {
			System.out.println("Can't Read repServers File");
			e.printStackTrace();
		}
	}

	public String read(String fileName) throws FileNotFoundException,
			IOException, RemoteException {
		if (metaData.containsKey(fileName)) {
			return metaData.get(fileName)[0];
		}
		return null;
	}

	public String newTxn(String fileName) throws RemoteException, IOException {
		long txnID = System.currentTimeMillis();
		Transaction transaction = new Transaction(fileName, txnID);
		transactions.put(txnID, transaction);
		if (!metaData.containsKey(fileName)) {
			int size = replicasIp.size();
			int primaryRep = random.nextInt(size);
			String[] replicas = { replicasIp.get(primaryRep),
					replicasIp.get((primaryRep + 1) % size),
					replicasIp.get((primaryRep + 2) % size) };
			metaData.put(fileName, replicas);
		}
		String data = txnID + "," + metaData.get(fileName)[0] + "," + txnID;
		return data;
	}
}
