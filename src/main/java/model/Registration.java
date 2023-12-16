package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;

public class Registration {

    private Connection con;
    HttpSession se;

    public Registration(HttpSession session) {
        try {

            Class.forName("com.mysql.jdbc.Driver"); // load the drivers
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DATA", "root", "Sagar");
            // connection with data base
            se = session;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Registration(String name, String phone, String email, String pw) {
        PreparedStatement ps;
        String status = "";
        try {
            Statement st = null;
            ResultSet rs = null;
            st = con.createStatement();
            rs = st.executeQuery("select * from sookshmas where phone='" + phone + "' or email='" + email + "';");
            boolean b = rs.next();
            if (b) {
                status = "existed";
            } else {
                ps = (PreparedStatement) con.prepareStatement("insert into data.sookshmas(name,phone,email,pw)values(?,?,?,?)");
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);
                ps.setString(4, pw);
                int a = ps.executeUpdate();
                if (a > 0) {
                    status = "success";
                } else {
                    status = "failure";
                }
            } 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public String login(String email, String pass) {
        String status1 = "", id = "";
        String name = "", emails = "";

        try {
            Statement st = null;
            ResultSet rs = null;
            st = con.createStatement();

            rs = st.executeQuery("select * from sookshmas where email='" + email + "' and pw='" + pass + "';");
            boolean b = rs.next();
            if (b == true) {
                id = rs.getString("slno");
                name = rs.getString("name");
                emails = rs.getString("email");
                se.setAttribute("uname", name);
                se.setAttribute("email", emails);
                se.setAttribute("id", id);
                status1 = "success";
            } else {
                status1 = "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status1;
    }


public Student getInfo() {
        Statement st = null;
        ResultSet rs = null;
        Student s = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from sookshmas where slno= '" + se.getAttribute("id") + "'");
            boolean b = rs.next();
            if (b == true) {
                s = new Student();
                s.setName(rs.getString("name"));
                s.setphone(rs.getString("phone"));
                s.setemail(rs.getString("email"));
            } else {
                s = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }
public String update(String name, String pno, String email) {
    String status = "";
    Statement st = null;
    ResultSet rs = null;
    try {
        st = con.createStatement();
        st.executeUpdate("update sookshmas set name='" + name + "',phone='" + pno + "',email='" + email + "' where slno= '" + se.getAttribute("id") + "' ");
        se.setAttribute("uname", name);
        status = "success";
    } catch (Exception e) {
        status = "failure";
        e.printStackTrace();
    }

    return status;
}
public String delete(int id) {
    int count = 0;
    Statement st = null;
    String status = "";
    try {
        st = con.createStatement();
        count = st.executeUpdate("delete from sookshmas1 where slno='" + id + "'");
        if (count > 0) {
            status = "success";
        } else {
            status = "failure";
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return status;
}
public ArrayList<Student> getUserDetails() {
	Statement st;
	ResultSet rs;
	ArrayList<Student> al = new ArrayList<Student>();
	try {
	st = con.createStatement();
	String qry = "select *, date_format(date, '%b %d, %Y') as date1 " +
            "from sookshmas1 " +
            "where slno not in (1);";

	rs = st.executeQuery(qry);
	while (rs.next()) {
	Student p = new Student();
	p.setId(rs.getString("slno"));
	p.setName(rs.getString("name"));
	p.setemail(rs.getString("email"));
	p.setphone(rs.getString("phone"));
	p.setdate(rs.getString("date1"));
	al.add(p);
	}
	} catch (Exception e) {
	e.printStackTrace();
	}
	return al;
	}

public ArrayList<Student> getUserinfo(String id) {
    Statement st = null;
    ResultSet rs = null;
    ArrayList<Student> al = new ArrayList<Student>();
    try {
        st = con.createStatement();
        String qry = "select * from sookshmas1 where slno = '" + id + "';";
        rs = st.executeQuery(qry);
        while (rs.next()) {
            Student p = new Student();
            p.setId(rs.getString("slno"));
            p.setName(rs.getString("name"));
            p.setemail(rs.getString("email"));
            p.setphone(rs.getString("phone"));
            p.setdate(rs.getString("date"));
            al.add(p);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return al;
}
public String forgot(String email)
{
	PreparedStatement ps=null;
	String status="";
	String Q1="SELECT * FROM sookshmas1 WHERE EMAIL=?";
	try
	{
		ResultSet rs=null;
		ps=con.prepareStatement(Q1);
		ps.setString(1, email);
		rs=ps.executeQuery();
		if(rs.next()==true)
		{
			String mail=rs.getString("email");
			se.setAttribute("email", mail);
			status="success";
		}
		else
		{
			status="failure";
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	
	return status;
}
public String change(String pwrd)
{
	PreparedStatement ps=null;
	String status="";
	String Q1="SELECT * FROM sookshmas1 WHERE EMAIL=?";
	String Q2="UPDATE sookshmas1 SET PASSWORD=? WHERE EMAIL=?";
	try
	{
		ResultSet rs=null;
		String em=(String)se.getAttribute("email");
		ps=con.prepareStatement(Q1);
		ps.setString(1, em);
		rs=ps.executeQuery();
		if(rs.next()==true)
		{
			String pd=rs.getString("PASSWORD");
			if(pd.equals(pwrd))
			{
				status="existed";
			}
		else
		{
			ps=con.prepareStatement(Q2);
			ps.setString(1, pwrd);
			ps.setString(2, em);
			int up=ps.executeUpdate();
			if(up>0)
			{
				status="success";
			}
			else
			{
				status="failure";
			}
		}
	}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return status;
}





}

