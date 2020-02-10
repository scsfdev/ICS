using System.Windows;
using Data_Viewer.ViewModel;
using GalaSoft.MvvmLight.Messaging;
using Data_Viewer.Model;

namespace Data_Viewer
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        /// <summary>
        /// Initializes a new instance of the MainWindow class.
        /// </summary>
        public MainWindow()
        {
            Messenger.Default.Register<string>(this, MyStatic.MsgType, ShowMsg);

            InitializeComponent();
            Closing += (s, e) => ViewModelLocator.Cleanup();
        }

        private void ShowMsg(string strMsg)
        {
            MessageBoxImage mboxImg;

            if (strMsg.ToUpper().Contains(MyStatic.ERROR))
                mboxImg = MessageBoxImage.Error;
            else if (strMsg.ToUpper().Contains(MyStatic.WARNING))
                mboxImg = MessageBoxImage.Warning;
            else
                mboxImg = MessageBoxImage.Information;

            Dispatcher.Invoke(() =>
            {
                MessageBox.Show(this, strMsg, MyStatic.Title, MessageBoxButton.OK, mboxImg);
            });
        }
    }
}