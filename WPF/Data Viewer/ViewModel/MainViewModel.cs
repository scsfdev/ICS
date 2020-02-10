using GalaSoft.MvvmLight;
using Data_Viewer.Model;
using System.Windows.Input;
using System;
using System.Windows.Threading;
using System.Collections.Generic;
using GalaSoft.MvvmLight.Messaging;
using System.Linq;
using GalaSoft.MvvmLight.Command;

namespace Data_Viewer.ViewModel
{
    
    public class MainViewModel : ViewModelBase
    {
        myDataModel myDM = new myDataModel();
        DbHelper myDB = new DbHelper();

        DispatcherTimer myTimer = new DispatcherTimer();

        #region Private Constant

        private const string tagIncoming = "INCOMING";
        private const string tagOutgoing = "OUTGOING";
        private const string tagStockIn = "STOCKIN";
        private const string tagStockOut = "STOCKOUT";

        #endregion


        #region TAG Binding

        public string TAG_INCOMING { get { return tagIncoming; } }
        public string TAG_OUTGOING
        {
            get { return tagOutgoing; }
        }
        public string TAG_STOCKIN
        {
            get { return tagStockIn; }
        }
        public string TAG_STOCKOUT
        {
            get { return tagStockOut; }
        }

        #endregion

        public ICommand CmdToggle { get; private set; }

        #region General Binding

        private string title;
        public string TITLE
        {
            get { return title; }
            set { Set(ref title, value); }
        }

        private string _dbStatusIndicator;
        public string DbStatusIndicator
        {
            get { return _dbStatusIndicator; }
            set { Set(ref _dbStatusIndicator, value); }
        }

        private string _dbStatusMsg;
        public string DbStatusMsg
        {
            get { return _dbStatusMsg; }
            set { Set(ref _dbStatusMsg, value); }
        }

        private string userName;
        public string User_Name
        {
            get { return userName; }
            set { Set(ref userName, value); }
        }

        private string userStatus;
        public string User_Status
        {
            get { return userStatus; }
            set { Set(ref userStatus, value); }
        }



        #endregion


        private void ToggleFunction(bool b10Sec)
        {
            myTimer.Stop();
            DataAsOf = "Data as of";

            if (b10Sec)
                myTimer.Interval = new TimeSpan(0, 0, 0, 10, 0);
            else
                myTimer.Interval = new TimeSpan(0, 0, 0, 5, 0);

            myTimer.Start();
        }

        public MainViewModel()
        {
            TITLE = MyStatic.Title;

            CmdToggle = new RelayCommand<bool>(ToggleFunction);

            User_Name = Environment.UserName;
            User_Status = "Machine: " + Environment.MachineName + Environment.NewLine +
                          "Domain: " + Environment.UserDomainName + Environment.NewLine +
                          "User: " + Environment.UserName;

            myTimer.Tick += MyTimer_Tick;

            ToggleFunction(false);
        }

        private void MyTimer_Tick(object sender, EventArgs e)
        {
            DataAsOf = "Data as of          " + DateTime.Now.ToString("yyyy-MMM-dd      |      hh:mm:ss tt");
            // 1. Get DB status.
            string strStatus = myDB.GetDbConnStatus();
            if (string.IsNullOrEmpty(strStatus))
            {
                DbStatusMsg = "Connected to: " + myDB.GetDbName();
                DbStatusIndicator = @"pack://application:,,,/Images/db-active.png";
            }
            else
            {
                DbStatusMsg = "Not connected!";
                DbStatusIndicator = @"pack://application:,,,/Images/db-inactive.png";
                return;
            }

            // 2. Pull data for INCOMING and OUTGOING.
            string strMsg = "";
            List<myWCF.Shipment> lstInOut = myDB.GetShipmentInOut(ref strMsg);
            if (!string.IsNullOrEmpty(strMsg))
            {
                ListIncoming = null;
                ListOutgoing = null;
                Messenger.Default.Send(strMsg, MyStatic.MsgType);
            }
            else
            {
                var vIn = (from s in lstInOut
                           where s.ShipIn == true
                           select s).ToList();

                var vOut = (from so in lstInOut
                            where so.ShipIn == false
                            select so).ToList();

                ListIncoming = vIn;

                ListOutgoing = vOut;
            }

            // 3. Pull data for OUTGOING.

            // 4. Pull data for STOCKIN & STOCKOUT.
            strMsg = "";
            List<myWCF.Stock> lstSIO = myDB.GetStockInOut(ref strMsg);
            if (!string.IsNullOrEmpty(strMsg))
            {
                ListStockIn = null;
                ListStockOut = null;
                Messenger.Default.Send(strMsg, MyStatic.MsgType);
            }
            else
            {
                var vIn = (from s in lstSIO
                           where s.InQty > 0 && s.OutQty <= 0
                           select s).ToList();

                var vOut = (from so in lstSIO
                            where so.InQty <= 0 && so.OutQty > 0
                            select so).ToList();

                ListStockIn = vIn;

                ListStockOut = vOut;
            }
        }

        public override void Cleanup()
        {
            // Clean up if needed
            if (myTimer.IsEnabled)
            {
                myTimer.Stop();
            }
                

            base.Cleanup();
        }


        private string dataAsOf;
        public string DataAsOf
        {
            get { return dataAsOf; }
            set { Set(ref dataAsOf, value); }
        }



        private List<myWCF.Shipment> listIncoming;
        public List<myWCF.Shipment> ListIncoming
        {
            get { return listIncoming; }
            set { Set(ref listIncoming, value); }
        }


        private List<myWCF.Shipment> listOutgoing;
        public List<myWCF.Shipment> ListOutgoing
        {
            get { return listOutgoing; }
            set { Set(ref listOutgoing, value); }
        }


        private List<myWCF.Stock> listStockIn;
        public List<myWCF.Stock> ListStockIn
        {
            get { return listStockIn; }
            set { Set(ref listStockIn, value); }
        }


        private List<myWCF.Stock> listStockOut;
        public List<myWCF.Stock> ListStockOut
        {
            get { return listStockOut; }
            set { Set(ref listStockOut, value); }
        }

        
    }
}