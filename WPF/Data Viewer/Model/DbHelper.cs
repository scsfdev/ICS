using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Reflection;


namespace Data_Viewer.Model
{
    // NOTE: For WCF service reference to return Generic List<T>, we need to change it from defualt Array[] to List<T> with below steps.
    // 1. Right click on WCF service reference.
    // 2. Choose Configure service reference.
    // 3. From Data Type >> Collection Type >> Change it from "System.Array" to "System.Collections.Generic.List".
    // 4. Update the reference to make sure it reflects the changes.

    class DbHelper : IDisposable
    {
        myWCF.WcfSvcICSClient wcfClient;

        public DbHelper()
        {
            try
            {
                wcfClient = new myWCF.WcfSvcICSClient("SoapEndPoint");
                wcfClient.Endpoint.Address = new System.ServiceModel.EndpointAddress(Properties.Settings.Default.WEB_API + "/soap");
            }
            catch (Exception e)
            {
                throw new Exception("DbHelper failed!", e);
            }
            
        }

        public string GetDbName()
        {
            myWCF.MyReply myR = wcfClient.GetDbInfo();
            if (myR.IsOk)
                return myR.Msg;
            else
                return "";
            
        }
        public string GetDbConnStatus()
        {
            myWCF.MyReply myR = wcfClient.CheckConn();
            if (myR.IsOk)
                return "";
            else
                return myR.Msg;
        }

        public List<myWCF.Shipment> GetShipmentInOut(ref string strMsg)
        {
            List<myWCF.Shipment> incoming = new List<myWCF.Shipment>();

            try
            {
                incoming = wcfClient.GetAllShipment();
            }
            catch (Exception e)
            {
                strMsg = MyStatic.ERROR + Environment.NewLine + 
                         "*Exception Error*" + Environment.NewLine +
                         "Details: " + e.Message;
            }

            return incoming;
        }


        public List<myWCF.Stock> GetStockInOut(ref string strMsg)
        {
            List<myWCF.Stock> stockIO = new List<myWCF.Stock>();

            try
            {
                stockIO = wcfClient.GetAllStock();
            }
            catch (Exception e)
            {
                strMsg = MyStatic.ERROR + Environment.NewLine +
                         "*Exception Error*" + Environment.NewLine +
                         "Details: " + e.Message;
            }

            return stockIO;
        }

        public void Dispose()
        {
            throw new NotImplementedException();
        }
    }
}
