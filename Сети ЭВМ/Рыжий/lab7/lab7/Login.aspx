<%@ Page Language="C#" Async="true" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="WebApplication1.Login" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Авторизация</title>
</head>
<body>
    <form id="form1" runat="server">
        <div>
            <asp:Table runat="server" Width="70%" HorizontalAlign="Center" CellPadding="8">
                <asp:TableRow>
                    <asp:TableCell ColumnSpan="2" HorizontalAlign="Center">
                        <h1>Форма авторизации</h1>
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell ColumnSpan="2 ">
                        <asp:Label ID="LabelError" runat="server" Text=""></asp:Label>
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell>
                        <asp:Label ID="Label2" runat="server" Text="Логин"></asp:Label>
                    </asp:TableCell>
                    <asp:TableCell Width="100%">
                        <asp:TextBox ID="LoginBox" runat="server" Width="100%" Font-Size="Large"></asp:TextBox>
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell>
                        <asp:Label ID="Label3" runat="server" Text="Пароль"></asp:Label>
                    </asp:TableCell>
                    <asp:TableCell Width="100%">
                        <asp:TextBox ID="PasswordBox" TextMode="Password" runat="server" Width="100%" Font-Size="Large"></asp:TextBox>
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell ColumnSpan="2">
                        <asp:Button ID="Button1" runat="server" OnClick="Login_Click" Width="100%" Font-Size="Large" Text="Войти" />
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell ColumnSpan="2">
                        <asp:HyperLink ID="HyperLink2" runat="server" Font-Size="Large" Width="100%"  NavigateUrl="~/Registration.aspx">Регистрация</asp:HyperLink>
                    </asp:TableCell>
                </asp:TableRow>
            </asp:Table>
        </div>
    </form>
</body>
</html>
