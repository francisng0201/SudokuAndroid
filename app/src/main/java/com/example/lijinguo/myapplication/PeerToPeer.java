//package com.example.lijinguo.myapplication;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.bluetooth.BluetoothDevice;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.CompoundButton;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//
//public class PeerToPeer extends Fragment {
//
//    private static final int ENABLE_BT_REQUEST_CODE = 1;
//    private static final int DISCOVERABLE_DURATION = 300;
//    private ListView listview;
//    private ToggleButton toggleButton;
//    private ArrayAdapter adapter;
//    BluetoothAdapter bluetoothAdapter;
//    View rootView;
//    // create in onCreateView?
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        rootView = inflater.inflate(R.layout.fragment_peer_to_peer, container, false);
//
//        return rootView;
//
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        toggleButton = (ToggleButton) rootView.findViewById(R.id.toggleButton);
//
//        listview = (ListView) rootView.findViewById(R.id.listView);
//
//        adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
//        listview.setAdapter(adapter);
////        adapter.add("abc");
//
//
//        // Inflate the layout for this fragment
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//
//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
//                onToggleClicked(toggleButton);
//            }
//        });
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String itemValue = (String) listview.getItemAtPosition(position);
//                String MAC = itemValue.substring(itemValue.length() - 17);
//                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);
//                // Initiate a connection request in a separate thread
//                Client client = new Client(bluetoothDevice, bluetoothAdapter);
//                client.start();
//                ListeningThread listener = new ListeningThread(getActivity(), bluetoothAdapter);
//                listener.start();
//            }
//        });
//
//    }
//
//
//
//
//    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            System.out.println("inside broadcast receiver");
//            String action = intent.getAction();
//            // Whenever a remote Bluetooth device is found
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                adapter.add(device.getName() + "\n" + device.getAddress());
//                System.out.println("device");
//                System.out.println(device.getName());
//            }
//        }
//    };
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ENABLE_BT_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(GlobalVariable.getAppContext(), "Ha! Bluetooth has been enabled.",
//                        Toast.LENGTH_SHORT).show();
//            } else { // RESULT_CANCELED as user refuse or failed
//                Toast.makeText(GlobalVariable.getAppContext(), "Bluetooth is not enabled.",
//                        Toast.LENGTH_SHORT).show();
//                toggleButton.setChecked(false);
//            }
//        }
//    }
//
//    public void onToggleClicked(View view) {
//
//        adapter.clear();
//
//        ToggleButton toggleButton = (ToggleButton) view;
//
//        if (bluetoothAdapter == null) {
//            System.out.println("This device doesn't support bluetooth");
//            toggleButton.setChecked(false);
//        } else {
//            if(toggleButton.isChecked()){
//                if (!bluetoothAdapter.isEnabled()) {
//                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
//                }
//
//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
////                filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//                getActivity().registerReceiver(broadcastReceiver, filter);
//
//                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
//                startActivityForResult(discoverableIntent, 0);
//                bluetoothAdapter.startDiscovery();
//
//
//
//
//            }else { // Turn off bluetooth
//
//                bluetoothAdapter.disable();
//                adapter.clear();
//            }
//        }
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        getActivity().unregisterReceiver(broadcastReceiver);
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        getActivity().registerReceiver(broadcastReceiver, filter);
//    }
//
//
//    /*Activity act;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_multiple_player);
//        this.act = this;
//
//        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
//
//        listview = (ListView) findViewById(R.id.listView);
//
//        adapter = new ArrayAdapter(this.getApplicationContext(), android.R.layout.simple_list_item_1);
//        listview.setAdapter(adapter);
////        adapter.add("abc");
//
//
//        // Inflate the layout for this fragment
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//
//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
//                onToggleClicked(toggleButton);
//            }
//        });
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String itemValue = (String) listview.getItemAtPosition(position);
//                String MAC = itemValue.substring(itemValue.length() - 17);
//                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);
//                // Initiate a connection request in a separate thread
//                Client client = new Client(bluetoothDevice, bluetoothAdapter);
//                client.start();
//                ListeningThread listener = new ListeningThread(act, bluetoothAdapter);
//                listener.start();
//            }
//        });
//
//
//    }
//
//
//
//
//
//    private static final int ENABLE_BT_REQUEST_CODE = 1;
//    private static final int DISCOVERABLE_DURATION = 300;
//    private ListView listview;
//    private ToggleButton toggleButton;
//    private ArrayAdapter adapter;
//    BluetoothAdapter bluetoothAdapter;
//    View rootView;
//    // create in onCreateView?
//
//
//
//
//
//
//    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            System.out.println("inside broadcast receiver");
//            String action = intent.getAction();
//            // Whenever a remote Bluetooth device is found
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                adapter.add(device.getName() + "\n" + device.getAddress());
//                System.out.println("device");
//                System.out.println(device.getName());
//            }
//        }
//    };
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ENABLE_BT_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(GlobalVariable.getAppContext(), "Ha! Bluetooth has been enabled.",
//                        Toast.LENGTH_SHORT).show();
//            } else { // RESULT_CANCELED as user refuse or failed
//                Toast.makeText(GlobalVariable.getAppContext(), "Bluetooth is not enabled.",
//                        Toast.LENGTH_SHORT).show();
//                toggleButton.setChecked(false);
//            }
//        }
//    }
//
//    public void onToggleClicked(View view) {
//
//        adapter.clear();
//
//        ToggleButton toggleButton = (ToggleButton) view;
//
//        if (bluetoothAdapter == null) {
//            System.out.println("This device doesn't support bluetooth");
//            toggleButton.setChecked(false);
//        } else {
//            if(toggleButton.isChecked()){
//                if (!bluetoothAdapter.isEnabled()) {
//                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
//                }
//
//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
////                filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//                this.registerReceiver(broadcastReceiver, filter);
//
//                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
//                startActivityForResult(discoverableIntent, 0);
//                bluetoothAdapter.startDiscovery();
//
//
//
//
//            }else { // Turn off bluetooth
//
//                bluetoothAdapter.disable();
//                adapter.clear();
//            }
//        }
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        this.unregisterReceiver(broadcastReceiver);
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        this.registerReceiver(broadcastReceiver, filter);
//    }
//
//*/
//}
