package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.time.LocalTime;

interface SmartHomeWindow{
	public void updateStatus(String status);
	public void updateTime(String startHour,String startMin,String endHour,String endMin);
}
class SmartHomeController{
	private SmartHomeWindow[] smartHomeWindowArray=new SmartHomeWindow[0];
	private String startHour;
	private String startMin;
	private String endHour;
	private String endMin;
	private int index;
	
	public void extendsArray(){
		SmartHomeWindow[] tempArray=new SmartHomeWindow[smartHomeWindowArray.length+1];
		System.arraycopy(smartHomeWindowArray,0,tempArray,0,smartHomeWindowArray.length);
		smartHomeWindowArray=tempArray;
	}
	public void addSmartHomeWindow(SmartHomeWindow smartHomeWindow){
		extendsArray();
		smartHomeWindowArray[smartHomeWindowArray.length-1]=smartHomeWindow;
	}	
	public void setStatus(String status){
		for(SmartHomeWindow s1:smartHomeWindowArray){
			s1.updateStatus(status);
		}
	}
	public void notifyTime(){
		smartHomeWindowArray[index].updateTime(startHour,startMin,endHour,endMin);
	}
	public void setTime(String startHour,String startMin,String endHour,String endMin){
		this.startHour=startHour;
		this.startMin=startMin;
		this.endHour=endHour;
		this.endMin=endMin;
		notifyTime();
	}
	public void setIndex(int index){
		this.index=index;
	}
}
class StatusWindow extends JFrame implements SmartHomeWindow{
	private int startHour;
	private int startMin;
	private int endHour;
	private int endMin;
	private JLabel onOffLbl;
	private JLabel lblStart;
	private JLabel lblEnd;
	private LocalTime currentTime;
	private Timer timer;
	private String timeStart;
	private String timeEnd;
	
	public StatusWindow(String name, int x){	
		setTitle(name);
		setSize(300,200);
		onOffLbl=new JLabel("OFF");
		setLayout(null);
		onOffLbl.setFont(new Font("",1,30));
		onOffLbl.setBounds(110,20,100,100);
		add(onOffLbl);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(x,500);		
		setVisible(true);
		
		lblStart = new JLabel();
        lblEnd = new JLabel();
	}
	public void updateStatus(String status){
		if(status.equals("ON")){
			 onOffLbl.setText("OFF");	 
		}else{
			 onOffLbl.setText("ON");
		}
	}
	public void updateTime(String startHour,String startMin,String endHour,String endMin){
		this.startHour=Integer.parseInt(startHour);
		this.startMin=Integer.parseInt(startMin);
		this.endHour=Integer.parseInt(endHour);
		this.endMin=Integer.parseInt(endMin);
		
		lblStart.setText(String.format("%02d:%02d:00",this.startHour,this.startMin));
		lblEnd.setText(String.format("%02d:%02d:00",this.endHour,this.endMin));
		timer = new Timer(1000, e -> {
            checkTime();
        });
        timer.start();
	
		
	}
	private void checkTime(){
		currentTime=LocalTime.now();
		timeStart=lblStart.getText();
		timeEnd=lblEnd.getText();
		if(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(timeStart)){
			onOffLbl.setText("ON");
		}else if(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(timeEnd)){
			onOffLbl.setText("OFF");
		}
	}
}
class TimeComponent extends JFrame{
	private JPanel timeSettingPnl;
	private JLabel startHourLbl;
	private JLabel startMinuteLbl;
	private JLabel endHourLbl;
	private JLabel endMinuteLbl;
	private JSpinner spinner1;
	private JSpinner spinner2;
	private JSpinner spinner3;
	private JSpinner spinner4;
	private JButton setBtn;
	private String startHour;
	private String startMin;
	private String endHour;
	private String endMin;
	
	private DefaultListModel<TimeModel> l1;
	private JList<TimeModel> list;
	
	private SmartHomeController smartHomeController;
	private TimeModel timeModel;
	
	public TimeComponent(String name,SmartHomeController smartHomeController){	
		this.smartHomeController=smartHomeController;
		setTitle(name);
		setFont(new Font("",1,30));
		setSize(500,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(925,100);	
		getContentPane().setBackground(new Color(153,255,255));
		setVisible(true);
		setLayout(new BorderLayout());
			
		l1=new DefaultListModel<>();
		list=new JList<>(l1);
			
		timeSettingPnl=new JPanel();
		timeSettingPnl.setBackground(new Color(128,128,128));
		
		startHourLbl=new JLabel("Start Hour");
		startHourLbl.setFont(new Font("",1,10));
		timeSettingPnl.add(startHourLbl);
		
		spinner1=new JSpinner(new SpinnerNumberModel(0,0,23,1));
		timeSettingPnl.add(spinner1);
		
		startMinuteLbl=new JLabel("Minute");
		startMinuteLbl.setFont(new Font("",1,10));
		timeSettingPnl.add(startMinuteLbl);

		spinner2=new JSpinner(new SpinnerNumberModel(0,0,59,1));
		timeSettingPnl.add(spinner2);
		
		endHourLbl=new JLabel("End Hour");
		endHourLbl.setFont(new Font("",1,10));
		timeSettingPnl.add(endHourLbl);
		
		spinner3=new JSpinner(new SpinnerNumberModel(0,0,23,1));
		timeSettingPnl.add(spinner3);
		
		endMinuteLbl=new JLabel("Minute");
		endMinuteLbl.setFont(new Font("",1,10));
		timeSettingPnl.add(endMinuteLbl);
		
		spinner4=new JSpinner(new SpinnerNumberModel(0,0,59,1));
		timeSettingPnl.add(spinner4);
		
		setBtn=new JButton("Set");
		setBtn.setFont(new Font("",1,10));
		setBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				timeModel=list.getSelectedValue();
				startHour=spinner1.getValue()+"";
				startMin=spinner2.getValue()+"";
				endHour=spinner3.getValue()+"";
				endMin=spinner4.getValue()+"";
				
				l1.addElement(new TimeModel(startHour,startMin,endHour,endMin));
				smartHomeController.setTime(startHour,startMin,endHour,endMin);
			}
		});
				
		add(new JScrollPane(list));
		timeSettingPnl.add(setBtn);
		add("South", timeSettingPnl);

	}
}
class TimeModel{
	private String startHour;
	private String startMin;
	private String endHour;
	private String endMin;
	
	public TimeModel(String startHour,String startMin,String endHour,String endMin){
		this.startHour=startHour;
		this.startMin=startMin;
		this.endHour=endHour;
		this.endMin=endMin;	
	}
	public void setStartHour(String startHour){
		this.startHour=startHour;
	}
	public void setStartMin(String startMin){
		this.startMin=startMin;
	}
	public void setEndHour(String endHour){
		this.endHour=endHour;
	}
	public void setEndMin(String endMin){
		this.endMin=endMin;
	}
	public String getStartHour(){
		return startHour;
	}
	public String getStartMin(){
		return startMin;
	}
	public String getEndHour(){
		return endHour;
	}
	public String getEndMin(){
		return endMin;
	}
	public String toString(){
		return "Starts at : "+startHour+"."+startMin+" Ends at : "+endHour+"."+endMin;
	}
}
class Settings extends JFrame{
	private DefaultListModel<String> l1;
	private JList<String> list;
	
	public Settings(SmartHomeController smartHomeController){
		setTitle("Settings");
		setSize(400,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(525,100);		
		
		l1=new DefaultListModel<>();
		l1.addElement("TV Living Room");
		l1.addElement("Speaker Living Room");
		l1.addElement("Window Living Room");
		l1.addElement("TV Dining Room");
		list=new JList<>(l1);
		
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {
					String value=list.getSelectedValue();
					int index=list.getSelectedIndex();
					smartHomeController.setIndex(index);
					new TimeComponent(value,smartHomeController);
				}
			}
		});
		add(new JScrollPane(list));
	}
}
class SmartHomeSystem extends JFrame{
	private JToggleButton onOffBtn;
	private JButton settingsBtn;
	private JLabel localTimeLbl;
	private Settings settings;
	
	public SmartHomeSystem(SmartHomeController smartHomeController){
		setTitle("Switch");
		setSize(400,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(125,100);
		setLayout(new GridLayout(3,1));
		
		onOffBtn=new JToggleButton("ON");
		onOffBtn.setFont(new Font("",1,30));
		onOffBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(onOffBtn.getText().equals("ON")){
					 onOffBtn.setText("OFF");	 
				}else{
					 onOffBtn.setText("ON");
				}
				smartHomeController.setStatus(onOffBtn.getText());
			}
		});
		add(onOffBtn);
		
		settingsBtn=new JButton("Settings");
		settingsBtn.setFont(new Font("",1,30));
		settingsBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(settings==null){
					settings=new Settings(smartHomeController);
				}
				settings.setVisible(true);
			}
		});
		add(settingsBtn);
		
		localTimeLbl=new JLabel();
		localTimeLbl.setFont(new Font("Arial", Font.PLAIN, 38));
		localTimeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		add(localTimeLbl);
		
		Timer timer=new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();		
    }
	private void updateTime(){
		SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
		String currentTime=dateFormat.format(new Date());
		localTimeLbl.setText(currentTime);
	}
}
class MainWindow{
	public static void main(String[] args){
		SmartHomeController smartHomeController=new SmartHomeController();
		smartHomeController.addSmartHomeWindow(new StatusWindow("TV Living Room",175));
		smartHomeController.addSmartHomeWindow(new StatusWindow("Speaker Living Room",475));
		smartHomeController.addSmartHomeWindow(new StatusWindow("Window Living Room",775));	
		smartHomeController.addSmartHomeWindow(new StatusWindow("TV Dining Room",1075));
		new SmartHomeSystem(smartHomeController).setVisible(true);	
	}
}
