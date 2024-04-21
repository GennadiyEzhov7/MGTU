using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;


namespace WebApplication1
{
    public partial class Login : System.Web.UI.Page
    {
        private SqlConnection sqlConnection = null;

        protected async void Page_Load(object sender, EventArgs e)
        {
            string connectionString = ConfigurationManager.ConnectionStrings["DBConnection"].ConnectionString;
            sqlConnection = new SqlConnection(connectionString);
            await sqlConnection.OpenAsync();
        }

        protected async void Login_Click(object sender, EventArgs e)
        {
            string loginBox = LoginBox.Text;
            string passBox = PasswordBox.Text;

            if (!validate(loginBox, passBox))
            {
                return;
            }

            string cmdSelLogPass = "SELECT [Password] FROM [Users] WHERE [Login] = @login";
            SqlCommand getUsersCredCmd = new SqlCommand(cmdSelLogPass, sqlConnection);
            getUsersCredCmd.Parameters.AddWithValue("@login", loginBox);

            string pass = Convert.ToString(await getUsersCredCmd.ExecuteScalarAsync());

            if (passBox == pass)
            {
                //HttpCookie login = new HttpCookie("login", LoginBox.Text);
                //HttpCookie sign = new HttpCookie("sign", lab7.SingGenerator.GetSign(LoginBox.Text + lab7.SingGenerator.signKey));
                //int timeOut = 30;
                //login.Expires = DateTime.Now.AddSeconds(timeOut);
                //sign.Expires = DateTime.Now.AddSeconds(timeOut);
                //Response.Cookies.Add(login);
                //Response.Cookies.Add(sign);
                Session["login"] = LoginBox.Text;
                Session["sign"] = lab7.SingGenerator.GetSign(LoginBox.Text + lab7.SingGenerator.signKey);
                Response.Redirect("UserPage.aspx", false);
            } else
            {
                LabelError.Text = "Неверный логин или пароль!";
            }
        }

        protected bool validate(string l, string p)
        {
            if (l.Equals("") || p.Equals(""))
            {
                LabelError.Text = "Логин и пароль не должны быть пустыми!";
                return false;
            }
            LabelError.Text = "";
            return true;
        }

        protected void Page_Unload(object sender, EventArgs e)
        {
            if (sqlConnection != null && sqlConnection.State != ConnectionState.Closed)
                sqlConnection.Close();
        }
    }
}