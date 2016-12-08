/* reference: android open source sample Bluetooth Chat.
 * 2014 The Android Open Source Project
 *
 */

package com.example.lijinguo.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import com.example.android.common.logger.Log;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    private Game game;
    ArrayList<Integer> sameRowCol;
    private CustomGridAdapter gridAdapter;
    private TextView target_view;
    private int last_position;
    public final int BOARD_LENGTH = 9;
    boolean notFinished = true;
    private String selected_number = "";
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private String board;
    private GridView gridView;
    private String[] items;
    private boolean[] emptyPosition;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
            System.out.println("setupChat");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peer_to_peer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        items = new String[81];
        emptyPosition = new boolean[81];
        for(int i = 0; i< items.length; i++){
            items[i] = "  ";
            emptyPosition[i] = true;
        }
        gridView = (GridView) view.findViewById(R.id.multi_player_grid);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(getActivity(), items, emptyPosition, items);
            gridView.setAdapter(gridAdapter);

        mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    game = new Game("easy");
                    items = game.getPuzzle();
                    emptyPosition = game.getEmptyPosition();
                    board =  TextUtils.join("|", items);
                    String message = board;
                    sendMessage(message);
                    gridAdapter = new CustomGridAdapter(getActivity(), items, emptyPosition, items);
                    gridView.setAdapter(gridAdapter);
                }
                sameRowCol = new ArrayList<Integer>();
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(target_view != null){
                        if(last_position == 21 || last_position == 24 || last_position == 48 || last_position == 51)
                            target_view.setBackgroundResource(R.drawable.border_left_bottom);
                        else if(last_position % 3 == 0 && last_position % 9 != 0)
                            target_view.setBackgroundResource(R.drawable.border_left);
                        else if((18<=last_position && last_position<=26)||(45<=last_position && last_position<=53)){
                            target_view.setBackgroundResource(R.drawable.border_bottom);
                        }
                        else{
                            target_view.setBackgroundResource(R.drawable.custom_button);
                        }

                    }
                    TextView tv = (TextView)((LinearLayout)view).getChildAt(0);
                    target_view = tv;
                    if(sameRowCol != null)
                        uncolorSameRowCol();
                    colorSameRowCol(position);

                    tv.setBackgroundResource(R.drawable.selected_cell);
                    last_position = position;

                }

            });

                LinearLayout layout = (LinearLayout) view.findViewById(R.id.addNumbers);

                LinearLayout middlelayer = new LinearLayout(getActivity());
                middlelayer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                for(int i = 0; i< 9;i++)
                {
                    //set the properties for button
                    Button btnTag = new Button(getActivity());

                    btnTag.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                            Button b = (Button)v;
                            String buttonText = b.getText().toString();
                            selected_number = buttonText;

                            boolean valid = false;
                            if(!selected_number.equals(""))
                                valid = game.validFill(last_position, selected_number);

                            if(valid){
                                items[last_position] = selected_number;
                                target_view.setText(selected_number);
                                // pass to controller
                            }
                            if(game.win()&& notFinished){
                                Toast.makeText(GlobalVariable.getAppContext(), "Congratualtions!",
                                        Toast.LENGTH_LONG).show();
                                System.out.println("Congratualtions!");
                                notFinished = false;

                            }
                        }
                    });

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 150);
                    int margin = (int) getResources().getDimension(R.dimen.gridMargin);
                    params.setMargins(margin, margin, margin, margin);
                    btnTag.setLayoutParams(params);
                    btnTag.setText(Integer.toString(i+1));

                    //add button to the layout
                    middlelayer.addView(btnTag);

                }

                layout.addView(middlelayer);
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);


        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    private void uncolorSameRowCol(){
        for(Integer loc: sameRowCol) {
            View view = gridView.getChildAt(loc);
            view.setBackgroundResource(R.color.cellColor);
        }
    }

    private void colorSameRowCol(int position){

        sameRowCol.clear();
        int row = position/BOARD_LENGTH;
        int col = position%BOARD_LENGTH;
        for(int i = col; i>= 0;i--){
            int pos = row*BOARD_LENGTH+i;
            sameRowCol.add(pos);
        }
        for(int j = col+1; j<=8; j++){
            int pos = row*BOARD_LENGTH+j;
            sameRowCol.add(pos);
        }
        for(int i = row; i>= 0;i--){
            int pos = i*BOARD_LENGTH+col;
            sameRowCol.add(pos);
        }
        for(int j = row+1; j<=8; j++){
            int pos = j*BOARD_LENGTH+col;
            sameRowCol.add(pos);
        }
        for(Integer loc: sameRowCol) {
            View view = gridView.getChildAt(loc);
            view.setBackgroundResource(R.color.hightlight);
        }
    }


        /**
         * Makes this device discoverable.
         */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
//            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    // TODO: can use this message to set board!!!!!
                    Toast.makeText(activity, "write"+ writeMessage,Toast.LENGTH_SHORT).show();
                    Game game = new Game("easy");
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(activity, "read"+readMessage,Toast.LENGTH_SHORT).show();
                    String[] received_board = readMessage.split("\\|");
                    for(int i = 0; i< received_board.length;i++) {
                        View hintView = gridView.getChildAt(i);
                        TextView tv = (TextView) ((LinearLayout) hintView).getChildAt(0);
                        tv.setTextColor(Color.BLACK);
                        tv.setText(received_board[i]);
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

}
