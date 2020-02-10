using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace myWCF
{
    [ServiceContract(Namespace = "http://10.72.137.98/myWCF/")]
    public interface IService1
    {
        [OperationContract]
        [WebInvoke(Method = "POST",
                     RequestFormat = WebMessageFormat.Json,
                     ResponseFormat = WebMessageFormat.Json,
                     UriTemplate = "/GenerateSQRC/")]
        MyReply GenerateSQRC(SQRC myCode);
    }


    [DataContract]
    public class MyReply
    {
        private bool _isOk;
        [DataMember]
        public bool IsOk
        {
            get { return _isOk; }
            set { _isOk = value; }
        }


        private string _msg;
        [DataMember]
        public string Msg
        {
            get { return _msg; }
            set { _msg = value; }
        }


        private byte[] _sqrcImage;
        [DataMember]
        public byte[] SqrcImage
        {
            get { return _sqrcImage; }
            set { _sqrcImage = value; }
        }


        public MyReply()
        {
            _isOk = false;
            _msg = "";
            _sqrcImage = null;
        }



    }


    [DataContract]
    public class SQRC
    {
        private string _userID;
        [DataMember]
        public string UserID
        {
            get { return _userID; }
            set { _userID = value; }
        }


        private string _publicData;
        [DataMember]
        public string PublicData
        {
            get { return _publicData; }
            set { _publicData = value; }
        }


        private string _privateData;
        [DataMember]
        public string PrivateData
        {
            get { return _privateData; }
            set { _privateData = value; }
        }


        private string _transactionNo;
        [DataMember]
        public string TransactionNo
        {
            get { return _transactionNo; }
            set { _transactionNo = value; }
        }
    }
}
