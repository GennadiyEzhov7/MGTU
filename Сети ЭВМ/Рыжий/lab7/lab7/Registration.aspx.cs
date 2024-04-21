using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;

namespace lab7
{
    public partial class Registration : System.Web.UI.Page
    {
        private SqlConnection sqlConnection = null;

        protected async void Page_Load(object sender, EventArgs e)
        {
            string connectionString = ConfigurationManager.ConnectionStrings["DBConnection"].ConnectionString;
            sqlConnection = new SqlConnection(connectionString);
            await sqlConnection.OpenAsync();
        }

        protected async void Reg_Click(object sender, EventArgs e)
        {
            string login_form = LoginBox.Text;
            string pass_form = PasswordBox.Text;

            if (!validation(login_form, pass_form))
            {
                return;
            }

            string cmdSelLogPass = "SELECT [Login] FROM [Users] WHERE [Login] = @login";
            SqlCommand getUsersCredCmd = new SqlCommand(cmdSelLogPass, sqlConnection);
            getUsersCredCmd.Parameters.AddWithValue("@login", login_form);

            string sqlLogin = Convert.ToString(await getUsersCredCmd.ExecuteScalarAsync());

            if (sqlLogin.Equals(""))
            {
                string cmdInsert = "INSERT INTO [Users] (Login, Password) VALUES (@login, @password)";
                SqlCommand regUser = new SqlCommand(cmdInsert, sqlConnection);
                regUser.Parameters.AddWithValue("@login", login_form);
                regUser.Parameters.AddWithValue("@password", pass_form);
                await regUser.ExecuteNonQueryAsync();
                Response.Redirect("Login.aspx", false);
            } else
            {
                ErrorLabel.Text = "Пользователь с таким логином уже существует!";
            }
        }

        protected bool validation(string l, string p)
        {
            if (l.Length < 5 || l.Length > 50)
            {
                ErrorLabel.Text = "Длина логина должны быть от 5 до 50 символов!";
                return false;
            }
            if (p.Length < 5 || l.Length > 50)
            {
                ErrorLabel.Text = "Длина пароля должна быть от 1 до 50 символов!";
                return false;
            }

            return true;
        }


        protected void Page_Unload(object sender, EventArgs e)
        {
            if (sqlConnection != null && sqlConnection.State != ConnectionState.Closed)
                sqlConnection.Close();
        }
    }
}