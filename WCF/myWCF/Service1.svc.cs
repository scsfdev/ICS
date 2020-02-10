using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Threading;
using System.Web;

namespace myWCF
{
    public class Service1 : IService1
    {
        string sqrcDir = HttpContext.Current.Server.MapPath(".") + @"\SQRC"; // Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "SQRC");
        string backupDir = HttpContext.Current.Server.MapPath(".") + @"\BACKUP"; // Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "SQRC");

        const int USER_ID = 0;
        const int TRANSACTION_NO = 1;
        const int OPEN_DATA = 2;
        const int CLOSE_DATA = 3;

        public MyReply GenerateSQRC(SQRC myCode)
        {
            MyReply myR = new MyReply();
            myR.IsOk = true;

            try
            {
                if (!Directory.Exists(sqrcDir))
                    Directory.CreateDirectory(sqrcDir);

                // 1. Generat text file.
                // 2. Wait for FileSystemWatcher to generate SQRC.
                // 3. If the picture file is there, read it and change to byte array and send it out.

                string myFile = Path.Combine(sqrcDir, myCode.TransactionNo + ".txt");
                string mySqrc = Path.Combine(sqrcDir, myCode.TransactionNo + ".jpg");

                string[] contents = new string[4];
                contents[USER_ID] = myCode.UserID;
                contents[TRANSACTION_NO] = myCode.TransactionNo;
                contents[OPEN_DATA] = myCode.PublicData;
                contents[CLOSE_DATA] = myCode.PrivateData;


                File.WriteAllLines(myFile, contents);

                // Wait for 20 seconds.
                int i = 1;
                bool bGetIt = false;
                do
                {
                    Thread.Sleep(500);

                    // If no file, sleep and wait again.
                    if (File.Exists(mySqrc))
                    {
                        bGetIt = true;
                        Thread.Sleep(500);
                        break;
                    }

                    i++;
                } while (i <= 20);


                // Read the picture in and convert to Byte[].
                if (bGetIt)
                {
                    myR.SqrcImage = File.ReadAllBytes(mySqrc);

                    // Backup pic.
                    File.Copy(mySqrc, Path.Combine(backupDir, myCode.TransactionNo + ".jpg"),true);
                   // File.Delete(mySqrc);
                   // File.Move(mySqrc, Path.Combine(backupDir, myCode.TransactionNo + ".jpg"));
                }
                else
                {
                    myR.Msg = "SQRC generating failed!";
                    myR.IsOk = false;
                }
            }
            catch (Exception e)
            {
                // Something wrong.
                myR.Msg = "Error: " + e.Message;
                myR.IsOk = false;
            }

            return myR;
        }
    }
}
