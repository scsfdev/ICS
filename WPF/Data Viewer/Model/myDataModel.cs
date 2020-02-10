using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data_Viewer.Model
{
    public class myDataModel
    {
        
    }

    public enum MsgType
    {
        MAIN_VIEW
    }

    public class MyStatic
    {
        public static string MsgType = "MAIN_VIEW";
        public static string Title = "Inventory Control System : Data Viewer";

        public const string ERROR = "<< ERROR >>";
        public const string WARNING = "<< WARNING >>";
        public const string INFO = "<< INFO >>";
    }
}
