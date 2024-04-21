using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication1
{
    public partial class Logout : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Session.Clear();
            Session.Abandon();
            Response.Cookies["ASP.NET_SessionId"].Expires = DateTime.Now.AddYears(-30);
            Response.Cookies["autorization"].Expires = DateTime.Now.AddYears(-30);
            Response.Cookies["sign"].Expires = DateTime.Now.AddYears(-30);
            Response.Redirect("login.aspx");
        }
    }
}