using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication1
{
    public partial class UserPage : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            //HttpCookie login = Request.Cookies["login"];
            //HttpCookie sign = Request.Cookies["sign"];

            if (Session["login"] != null && Session["sign"] != null)
            {
                string login = Session["login"].ToString();
                string sign = Session["sign"].ToString();

                //if (sign.Value == lab7.SingGenerator.GetSign(login.Value + lab7.SingGenerator.signKey))
                if (sign == lab7.SingGenerator.GetSign(login + lab7.SingGenerator.signKey))
                {
                    Label1.Text = "Добро пожаловать, " + login;
                    Response.Cookies["sign"].Expires = DateTime.Now.AddYears(-30);
                    return;
                }
            }
            Response.Redirect("login.aspx");
        }

        protected void Logout_Click(object sender, EventArgs e)
        {
            Response.Redirect("Logout.aspx");
        }

    }
}