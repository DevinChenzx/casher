// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NetSignServer.java

package ebank.infosec;

import cn.com.infosec.jce.exception.*;
import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.netsigninterface.*;
import cn.com.infosec.netsigninterface.exceptions.*;
import cn.com.infosec.netsigninterface.util.TrustCerts;
import cn.com.infosec.netsigninterface.util.TrustConfig;
import cn.com.infosec.util.Base64;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.infosec.CRLDownLoader;
import com.infosec.CRLDownLoaderFactory;
import com.infosec.EncCerts;
import com.infosec.NetSignX509CRL;
import com.infosec.ThreadManager;

// Referenced classes of package com.infosec:
//            NetSignX509CRL, LogWriter, CRLDownLoader, CRLDownLoaderFactory, 
//            ThreadManager, EncCerts

public class NetSignServer
{
	private static Logger log = Logger.getLogger(NetSignServer.class);

    private static String getprefix4file(String fn)
    {
        int i = fn.lastIndexOf(".");
        return fn.substring(0, i);
    }

  

   

    public static void updatCRLs(String CRLDIR)
    {      
        crlLoadReady = false;
        File f = new File(CRLDIR);
        File ftmp[] = f.listFiles();
        HashMap tabtmp = (HashMap)CRLTables.get(CRLDIR);
        CertificateFactory crlFact = null;
        try
        {
            crlFact = CertificateFactory.getInstance("X.509", "INFOSEC");
        }
        catch(Exception ex)
        {
            log.debug("generate Certifacte Factory of CRL Failed for" + ex.getMessage());
            return;
        }
        long start = System.currentTimeMillis();
        for(int i = 0; i < ftmp.length; i++)
            try
            {
                FileInputStream fis = new FileInputStream(ftmp[i]);
                X509CRL x509crl = (X509CRL)crlFact.generateCRL(fis);
                if(x509crl != null)
                {
                    tabtmp.put(ftmp[i].getName().toLowerCase(), NetSignX509CRL.getInstance(x509crl));
                    fis.close();                    
                }
            }
            catch(Exception ex)
            {
            	log.debug("generate Certifacte  CRL Failed for" + ex.getMessage());
            }

        CRLTables.put(CRLDIR, tabtmp);
        long end = System.currentTimeMillis();
        log.debug("Load CRL Cost Time " + (end - start));
        crlLoadReady = true;
        firstCRLLoadReady = true;
        long stop = System.currentTimeMillis();
        log.debug("CRL load finished , cost " + (stop - start));
    }

    public static void saveCRLTables(String file)
    {
        try
        {
            while(!crlLoadReady) 
                Thread.sleep(100L);
            FileOutputStream out = new FileOutputStream(file);
            if(lastCRLPortal == null)
                saveAllCRLTables(out);
            else
                saveCRLTable(out);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void saveCRLTable(OutputStream out)
    {
        try
        {
            Set crldirs = CRLTables.keySet();
            out.write(("CRLTables count " + CRLTables.size() + " [ save time " + (new Date()).toString() + "]\n").getBytes());
            out.flush();
            Object ks[] = crldirs.toArray();
            int i = 0;
            for(int length = ks.length; i < length; i++)
            {
                String crldir = (String)ks[i];
                String crltmp = lastCRLPortal;
                out.write(("The crls of " + crltmp).getBytes());
                HashMap dircrls = (HashMap)CRLTables.get(crldir);
                NetSignX509CRL nscrl = (NetSignX509CRL)dircrls.get(crltmp);
               // if(nscrl != null)
               //     nscrl.write(out);
                out.write("\n".getBytes());
                out.flush();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void saveAllCRLTables(OutputStream out)
    {
        try
        {
            Set crldirs = CRLTables.keySet();
            out.write(("CRLTables count " + CRLTables.size() + " [ save time " + (new Date()).toString() + "]\n").getBytes());
            out.flush();
            Object dirs[] = crldirs.toArray();
            int j = 0;
            for(int l = dirs.length; j < l; j++)
            {
                String crldir = (String)dirs[j];
                out.write(("The crls of " + crldir).getBytes());
                out.flush();
                HashMap dircrls = (HashMap)CRLTables.get(crldir);
                if(dircrls.size() > 0)
                {
                    out.write((" count " + dircrls.size() + "\n").getBytes());
                    Set crls = dircrls.keySet();
                    Object keys[] = crls.toArray();
                    int i = 0;
                    for(int length = keys.length; i < length; i++)
                    {
                        String crlPortal = (String)keys[i];
                        NetSignX509CRL nscrl = (NetSignX509CRL)dircrls.get(crlPortal);
                        out.write(("\tThe ids of " + crlPortal + " count " + nscrl.size() + "\n").getBytes());
                        if(nscrl != null)
                            //nscrl.write(out);
                        out.write("\n".getBytes());
                        out.flush();
                    }

                } else
                {
                    out.write("\n".getBytes());
                }
                out.write("\n".getBytes());
                out.flush();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    protected synchronized void setApi()
    {
        if(isAPIPropertySetted)
            return;
        try
        {
            log.debug(new Date() + "NetSignServer initializer starting...");
            ResourceBundle p = ResourceBundle.getBundle("netsign");
            log.debug("Find netsign.properties file");
            
            String trustindexs = new String(p.getString("trustindexs").getBytes("ISO8859-1"));
            int trusts = Integer.parseInt(trustindexs);
            log.debug("trusts:" + trusts);
            CRLDownLoader crls[] = new CRLDownLoader[trusts];
            String cldpflag = new String(p.getString("cldpflag"));
            String crldownloadinterval = new String(p.getString("crldownloadinterval"));            
            log.debug("crldownloadinterval:" + crldownloadinterval);
            String crldir = new String(p.getString("crlspath"));
            log.debug("crlspath:" + crldir);   
            
            for(int trusti = 0; trusti < trusts; trusti++)
            {            	
                String truststore = new String(p.getString("truststore" + trusti));
                log.debug("truststore:" + truststore);                        
                TrustConfig t1 = new TrustConfig();
                t1.setcrldir(crldir);
                t1.setiscrldp(cldpflag);
                log.debug("set root cert");
                t1.setrootcert(truststore);
                log.debug("set root cert ok");
                t1.setcrldownloadinterval(crldownloadinterval);
                Trustconfgtable.put(t1.getrootcertdn(), t1);
                crls[trusti] = CRLDownLoaderFactory.generateFileCRL(crldir, Integer.parseInt(crldownloadinterval));
                CRLTables.put(crldir, new HashMap());
            }

            trustCerts.setTrustCertConfig(Trustconfgtable);
            ThreadManager.startTreads(crls);
            String pfxdirpath = new String(p.getString("pfxdirpath").getBytes("ISO8859-1")) + "/";
            log.debug("pfxdirpath:" + pfxdirpath);
            int pfxindexs = Integer.parseInt(p.getString("pfxindexs"));
            ServerKeyStore keystores[] = new ServerKeyStore[pfxindexs];
            FileInputStream fpfx = null;
            for(int j = 0; j < pfxindexs; j++)
            {
                fpfx = new FileInputStream(pfxdirpath + new String(p.getString("pfx" + String.valueOf(j)).getBytes("ISO8859-1")));
                log.debug("pfx dir=" + pfxdirpath + new String(p.getString("pfx" + String.valueOf(j)).getBytes("ISO8859-1")));
                String pwdpfx = p.getString("passwordpfx" + String.valueOf(j));
                ServerKeyStore pfxstore = ServerKeyStoreFactory.generatePKCS12ServerKeyStore(fpfx, pwdpfx.toCharArray());
                keystores[j] = pfxstore;
                fpfx.close();
            }

            log.debug("NetSignServer initializering: Get pfx Certificate success!");
            EncCerts enccert = new EncCerts(new String(p.getString("enccert").getBytes("ISO8859-1")));
            encCertTable = enccert.getCerts();
            for(int j = 0; j < keystores.length; j++)
            {
                log.debug(keystores[j].getCertDN());
                ServkeyStoreTable.put(keystores[j].getCertDN().toLowerCase(), keystores[j]);
            }

            log.debug("NetSignServer initializer finished successfully!");           
        }
        catch(InvalidParameterException e0)
        {
            e0.printStackTrace();
            log.debug( "InitialParams Error! Error maybe in ServerKeystore/TrustCerts/CRLS.");
            log.debug("NetSignServer initializer failed to start, InvalidParameterException.");
        }
        catch(FileNotFoundException e1)
        {
            e1.printStackTrace();
            log.debug( "Failed Initializer! Can't findout ServerKeystore.");
            log.debug("NetSignServer initializer failed to start, cannot found Server Certificate.");
            log.debug(e1.getMessage());
        }
        catch(ServerKeyStoreException e2)
        {
            e2.printStackTrace();
            log.debug( "Failed Initializer! Can't open ServerKeystore.");
            log.debug("NetSignServer initializer failed to start,Server Certificate error.");
            log.debug(e2.getMessage());
        }
        catch(TrustCertException tex)
        {
            tex.printStackTrace();
            log.debug( "Failed Initializer! Trust Cert Can not Open.");
            log.debug("NetSignServer initializer failed to start,configuration file error.");
            log.debug(tex.getMessage());
        }
        catch(IOException ioex)
        {
            ioex.printStackTrace();
            log.debug(ioex.getMessage());
        }
        isAPIPropertySetted = true;
    }

    public NetSignServer()
    {
        netsignapi = new NetSignImpl();
        m_formContents = null;
        m_nameOfFiles = null;
        plainText = null;
        errorNum = 0;
        setApi();
    }

    public String getDefaultSignerDN()
    {
        Enumeration en = ServkeyStoreTable.keys();
        String defaultdn;
        for(defaultdn = null; en.hasMoreElements(); defaultdn = (String)en.nextElement());
        return defaultdn;
    }

    public String NSBase64Encode(String base64ToBeEncodedText)
    {
        String tmp = null;
        try
        {
            tmp = new String(netsignapi.Base64Encode(base64ToBeEncodedText.getBytes()));
        }
        catch(IOException ex)
        {
            errorNum = -10046;
        }
        return tmp;
    }

    public String NSBase64Decode(String base64ToBeDecodedText)
    {
        String tmp = null;
        try
        {
            tmp = new String(netsignapi.Base64Deccode(base64ToBeDecodedText));
        }
        catch(IOException ex)
        {
            errorNum = -10046;
        }
        return tmp;
    }

    public String NSHashAndBase64Encode(String plainText)
        throws IOException
    {
        String tmp = null;
        try
        {
            tmp = new String(netsignapi.hash1Base64(plainText.getBytes()));
        }
        catch(IOException ex)
        {
            errorNum = -10046;
        }
        return tmp;
    }

    public void NSAddFormItem(String itemContent)
    {
        String str1 = "00000000";
        int len = itemContent.length();
        if(len == 0)
        {
            errorNum = -10026;
            return;
        }
        String str2 = Integer.toString(len, 10);
        String str;
        if(m_formContents == null)
            str = str1.substring(0, 8 - str2.length()) + str2 + itemContent;
        else
            str = new String(m_formContents) + str1.substring(0, 8 - str2.length()) + str2 + itemContent;
        m_formContents = str.getBytes();
    }

    public void NSAddFile(String Filename)
    {
        int len = Filename.length();
        if(len == 0)
        {
            errorNum = -10025;
            return;
        }
        String str;
        if(m_nameOfFiles == null)
            str = Filename;
        else
            str = new String(m_nameOfFiles) + "\t" + Filename;
        m_nameOfFiles = str.getBytes();
    }

    public void NSSetPlainText(byte plaintext[])
    {
        plainText = plaintext;
    }

    public byte[] NSGetFormItem()
    {
        if(totalFormLen == 0 || formCounter >= totalFormLen)
            return new byte[0];
        else
            return formItems[formCounter++].getBytes();
    }

    private void getTotle()
    {
        getFormFiles();
        getFormItems();
    }

    private void getFormItems()
    {
        byte item[] = (byte[])null;
        Vector v = new Vector();
        byte plainContent[] = netsignapi.getContentData();
        if(plainContent != null)
        {
            int pos = 0;
            int formsLen = Integer.parseInt(new String(plainContent, pos, 8), 10);
            pos += 8;
            if(formsLen == 0 || pos > formsLen + 7)
            {
                formItems = null;
                errorNum = -10024;
            } else
            {
                int iLen;
                for(; pos < formsLen; pos += iLen)
                {
                    iLen = Integer.parseInt(new String(plainContent, pos, 8), 10);
                    pos += 8;
                    item = new byte[iLen];
                    System.arraycopy(plainContent, pos, item, 0, iLen);
                    v.add(new String(item));
                }

            }
            int size = v.size();
            formItems = new String[size];
            for(int i = 0; i < size; i++)
                formItems[i] = (String)v.get(i);

            v = null;
            if(formItems != null && formItems.toString().length() > 1)
                totalFormLen = formItems.length;
            formCounter = 0;
        } else
        {
            errorNum = -10006;
        }
    }

    private void getFormFiles()
    {
        Hashtable items = null;
        byte plainContent[] = netsignapi.getContentData();
        if(plainContent != null)
        {
            int pos = Integer.parseInt(new String(plainContent, 0, 8), 10) + 16;
            items = new Hashtable();
            int fLen = 0;
            int fNameLen = 0;
            String fName = "";
            byte fContent[] = (byte[])null;
            while(pos < plainContent.length) 
            {
                fLen = Integer.parseInt(new String(plainContent, pos, 8), 10);
                pos += 8;
                fNameLen = Integer.parseInt(new String(plainContent, pos, 8), 10);
                pos += 8;
                fName = new String(plainContent, pos, fNameLen);
                pos += fNameLen;
                fContent = new byte[fLen - fNameLen - 8];
                System.arraycopy(plainContent, pos, fContent, 0, fLen - fNameLen - 8);
                pos += fLen - fNameLen - 8;
                items.put(fName, fContent);
            }
            fileItems = items;
            totalFileLen = fileItems.size();
            fileCounter = 0;
        } else
        {
            errorNum = -10006;
        }
    }

    public String getCertExtensionValue(String oid)
    {
        return netsignapi.getCertExtensionValue(oid);
    }

    public int getLastErrnum()
    {
        int tmp = errorNum;
        errorNum = 0;
        return tmp;
    }

    private void makePlainText()
    {
        int totalFormContLen = 0;
        int totalFileLen = 0;
        int totalLen = 0;
        if(m_formContents != null)
            totalFormContLen = m_formContents.length;
        else
            totalFormContLen = 0;
        String str1 = "00000000";
        String str2 = Integer.toString(totalFormContLen, 10);
        String strForm;
        if(m_formContents != null)
            strForm = str1.substring(0, 8 - str2.length()) + str2 + new String(m_formContents);
        else
            strForm = str1.substring(0, 8 - str2.length()) + str2;
        String strFile = "";
        if(m_nameOfFiles != null)
        {
            int startIndex = 0;
            int searchedIndex = 0;
            int fileLenSubTotal = 0;
            int MAXFILELENGTH = 0x100000;
            String StrFileNameParse = new String(m_nameOfFiles) + "\t";
            while((searchedIndex = StrFileNameParse.indexOf("\t", startIndex)) != -1) 
            {
                String fileName = StrFileNameParse.substring(startIndex, searchedIndex);
                int nameLen = fileName.length();
                File f = new File(fileName);
                long fileLen = f.length();
                if(fileLen > (long)MAXFILELENGTH)
                {
                    log.debug("File size should not be larger than " + Integer.toString(MAXFILELENGTH, 10));
                    errorNum = -10027;
                    return;
                }
                byte filecontent[] = new byte[(int)fileLen];
                try
                {
                    FileInputStream fis = new FileInputStream(f);
                    if((long)fis.read(filecontent) != fileLen)
                    {
                        log.debug("Reading of file is incomplete");
                        errorNum = -10025;
                        return;
                    }
                    fis.close();
                }
                catch(FileNotFoundException exc)
                {
                    log.debug("File not found");
                    errorNum = -10025;
                    return;
                }
                catch(IOException exc)
                {
                    log.debug("File not found");
                    errorNum = -10025;
                    return;
                }
                str2 = Integer.toString(nameLen, 10);
                String str3 = Integer.toString((int)fileLen + nameLen + 8, 10);
                String StrFileSubTotal = strFile + str1.substring(0, 8 - str3.length()) + str3 + str1.substring(0, 8 - str2.length()) + str2 + fileName + new String(filecontent);
                fileLenSubTotal = (int)((long)fileLenSubTotal + ((long)(16 + nameLen) + fileLen));
                strFile = StrFileSubTotal;
                startIndex = searchedIndex + 1;
            }
            totalFileLen = fileLenSubTotal;
        } else
        {
            totalFileLen = 0;
        }
        str2 = Integer.toString(totalFileLen, 10);
        String strFinal = strForm + str1.substring(0, 8 - str2.length()) + str2 + strFile;
        totalLen = totalFormContLen + totalFileLen + 16;
        AttachedSignContent = strFinal.getBytes();
        m_formContents = null;
        m_nameOfFiles = null;
    }

    public String NSGetSignerCertInfo(int type)
    {
        return netsignapi.getSignCertInfo(type);
    }

    public String NSGetReciptCertInfo(int type)
    {
        return netsignapi.getEncCertInfo(type);
    }

    public byte[][] NSGetFileInfo()
    {
        byte fileInfo[][] = new byte[2][];
        if(totalFileLen == 0 || fileCounter >= totalFileLen)
            return new byte[0][];
        Enumeration tmpEnu = fileItems.keys();
        for(int k = 0; k < fileCounter; k++)
            tmpEnu.nextElement();

        String tmpString1 = (String)tmpEnu.nextElement();
        fileInfo[0] = null;
        fileInfo[0] = tmpString1.getBytes();
        fileInfo[1] = (byte[])fileItems.get(new String(fileInfo[0]));
        fileCounter++;
        return fileInfo;
    }

    public byte[] NSGetPlainText()
    {
        return netsignapi.getContentData();
    }

    public byte[] NSAttachedSign(String deSignerDN)
    {
        Object obj = ServkeyStoreTable.get(deSignerDN.toLowerCase());
        if(obj == null)
        {
            errorNum = -10028;
            return null;
        }
        ServerKeyStore pfx = (ServerKeyStore)obj;
        byte tmp1[] = (byte[])null;
        byte tmp[];
        if(plainText == null)
        {
            makePlainText();
            tmp = AttachedSignContent;
        } else
        {
            tmp = plainText;
        }
        if(errorNum != 0)
            return null;
        try
        {
            tmp1 = Base64.encode(netsignapi.GenerateSingleSignedMsg(tmp, pfx, false)).getBytes();
        }
        catch(ServerKeyStoreException expfx)
        {
            expfx.printStackTrace();
            errorNum = -10047;
        }
        catch(InvalidKeyException exin)
        {
            errorNum = -10042;
        }
        catch(NoSuchProviderException exp)
        {
            errorNum = -10044;
        }
        catch(NoSuchAlgorithmException exa)
        {
            errorNum = -10043;
        }
        catch(SignatureException exs)
        {
            errorNum = -10045;
        }
        catch(IOException exbase)
        {
            errorNum = -10046;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            errorNum = -10010;
        }
        return tmp1;
    }

    /**
     * @deprecated Method NetSignCommonSignVerify is deprecated
     */

    public int NetSignCommonSignVerify(String msg)
    {
        int tmpInt = -1;
        try
        {
            byte tempB[] = Base64.decode(msg);
            netsignapi.VerifySingleSignedMsg(tempB, trustCerts, CRLTables);
            return 1;
        }
        catch(CertificateException e0)
        {
            return -14;
        }
        catch(CRLException e2)
        {
            return -17;
        }
        catch(InvalidCertificateException e1)
        {
            return tmpInt = -2;
        }
        catch(Exception e1)
        {
            return tmpInt;
        }
    }

    /**
     * @deprecated Method GetCommonPlainText is deprecated
     */

    public byte[] GetCommonPlainText()
    {
        return netsignapi.getContentData();
    }

    public byte[] NSRAWSign(byte plainText[], String subject)
    {
        if(plainText == null || plainText.length == 0)
        {
            errorNum = -10062;
            return null;
        }
        ServerKeyStore store = (ServerKeyStore)ServkeyStoreTable.get(subject.toLowerCase());
        if(store == null)
        {
            errorNum = -10028;
            return null;
        }
        byte bResult[] = (byte[])null;
        try
        {
            bResult = netsignapi.rawSign(store.getPrivateKey(), plainText);
        }
        catch(ServerKeyStoreException e1)
        {
            errorNum = -10047;
            return null;
        }
        catch(RAWSignException e2)
        {
            errorNum = -10063;
            log.debug( "NSRAWSign error:" + e2.getMessage());
            e2.printStackTrace();
            return null;
        }
        try
        {
            return Base64.encode(bResult).getBytes();
        }
        catch(IOException e)
        {
            errorNum = -10064;
        }
        return null;
    }

    public void NSVerifyPlainSignedMsg(byte plainText[], String Base64plainsignedMsg, X509Certificate SignedCert)
    {
        try
        {
            netsignapi.VerifySinglePlainSignedMsg(Base64.decode(Base64plainsignedMsg), plainText, trustCerts, CRLTables, SignedCert);
        }
        catch(IOException ioex)
        {
            ioex.printStackTrace();
        }
        catch(CRLException ex1)
        {
            errorNum = -10038;
        }
        catch(InvalidCertificateException ex2)
        {
            errorNum = Integer.parseInt(ex2.getMessage());
        }
        catch(VerifyPlainSignedMsgException ex3)
        {
            ex3.printStackTrace();
            log.debug( "NSVerifyPlainSignedMsg error:" + ex3.getMessage());
            errorNum = -10061;
        }
    }

    private static X509Certificate getX509Cert(String certpath)
        throws Exception
    {
        X509Certificate cert = null;
        FileInputStream finput = null;
        try
        {
            finput = new FileInputStream(certpath);
        }
        catch(FileNotFoundException fnotex)
        {
            throw new Exception("VerifyCert:Can not Found RootCert " + certpath);
        }
        try
        {
            CertificateFactory certfactory = CertificateFactory.getInstance("X.509", "INFOSEC");
            cert = (X509Certificate)certfactory.generateCertificate(finput);
        }
        catch(Exception ex)
        {
            throw new Exception("VerifyCert:Can not Get Root Cert" + ex.getMessage());
        }
        return cert;
    }

    public void NSAttachedVerify(byte signedMsg[])
    {
        try
        {
            netsignapi.VerifySingleSignedMsg(Base64.decode(signedMsg), trustCerts, CRLTables);
        }
        catch(CertificateException ex)
        {
            errorNum = -10040;
        }
        catch(CRLException ex1)
        {
            errorNum = -10038;
        }
        catch(InvalidCertificateException ex2)
        {
            errorNum = Integer.parseInt(ex2.getMessage());
        }
        catch(Throwable ex3)
        {
            ex3.printStackTrace();
            errorNum = -10029;
        }
    }

    public X509Certificate NSGetSignCert()
    {
        return netsignapi.getSignCertEntity();
    }

    public byte[] NSDetachedSign(String deSignerDN)
    {
        Object obj = ServkeyStoreTable.get(deSignerDN.toLowerCase());
        if(obj == null)
        {
            errorNum = -10028;
            return null;
        }
        ServerKeyStore pfx = (ServerKeyStore)obj;
        byte tmp1[] = (byte[])null;
        byte tmp[];
        if(plainText == null)
        {
            makePlainText();
            tmp = AttachedSignContent;
        } else
        {
            tmp = plainText;
        }
        if(errorNum != 0)
            return null;
        try
        {
            tmp1 = Base64.encode(netsignapi.GenerateSingleSignedMsg(tmp, pfx, true)).getBytes();
        }
        catch(ServerKeyStoreException expfx)
        {
            errorNum = -10047;
        }
        catch(InvalidKeyException exin)
        {
            errorNum = -10042;
        }
        catch(NoSuchProviderException exp)
        {
            errorNum = -10044;
        }
        catch(NoSuchAlgorithmException exa)
        {
            errorNum = -10043;
        }
        catch(SignatureException exs)
        {
            errorNum = -10045;
        }
        catch(IOException exbase)
        {
            errorNum = -10046;
        }
        catch(Throwable ex)
        {
            errorNum = -10010;
        }
        return tmp1;
    }

    public void NSDetachedVerify(byte signedMsg[], byte plainText[])
    {
        try
        {
            netsignapi.VerifySingleSignedMsg(Base64.decode(signedMsg), plainText, trustCerts, CRLTables);
        }
        catch(CertificateException ex)
        {
            errorNum = -10039;
        }
        catch(CRLException ex1)
        {
            errorNum = -10039;
        }
        catch(InvalidCertificateException ex2)
        {
            errorNum = Integer.parseInt(ex2.getMessage());
        }
        catch(Throwable ex3)
        {
            ex3.printStackTrace();
            errorNum = -10030;
        }
    }

    public byte[] NSEncryptedEnvelop(String deReciptDN)
    {
        Object obj = encCertTable.get(deReciptDN);
        if(obj == null)
        {
            errorNum = -10031;
            return null;
        }
        X509Certificate cert = (X509Certificate)obj;
        byte tmp1[] = (byte[])null;
        byte tmp[];
        if(plainText == null)
        {
            makePlainText();
            tmp = AttachedSignContent;
        } else
        {
            tmp = plainText;
        }
        if(errorNum != 0)
            return null;
        try
        {
            tmp1 = Base64.encode(netsignapi.composeSingleEnvelopedMsg(tmp, cert)).getBytes();
        }
        catch(WriteEnvDataException wrdndateex)
        {
            errorNum = -10049;
        }
        catch(EncryptKeyException exenc)
        {
            errorNum = -10050;
        }
        catch(EncryptDataException encdaex)
        {
            errorNum = -10051;
        }
        catch(EncryptAlgException ealgex)
        {
            errorNum = -10052;
        }
        catch(CertificateException cex)
        {
            errorNum = -10040;
        }
        catch(InvalidCertificateException invlidex)
        {
            errorNum = -10041;
        }
        catch(NoSuchProviderException noproex)
        {
            errorNum = -10044;
        }
        catch(IOException exio)
        {
            errorNum = -10046;
        }
        catch(Throwable ex)
        {
            errorNum = -10032;
            log.debug( "NSEncryptedEnvelop error:" + ex.getMessage());
            log.debug("NSEncryptedEnvelop error:" + ex.getMessage());
        }
        return tmp1;
    }

    public void NSDecryptedEnvelop(byte envelopedMsg[], String recipDN)
    {
        Object obj = ServkeyStoreTable.get(recipDN.toLowerCase());
        if(obj == null)
        {
            errorNum = -10028;
            return;
        }
        ServerKeyStore pfx = (ServerKeyStore)obj;
        try
        {
            netsignapi.decomposeSingleEnvelopedMsg(Base64.decode(envelopedMsg), pfx);
        }
        catch(CertificateException certex)
        {
            errorNum = -10040;
        }
        catch(InvalidCertificateException invalidex)
        {
            errorNum = -10041;
        }
        catch(DecryptKeyException dekeex)
        {
            errorNum = -10048;
        }
        catch(DecryptDataException dedaex)
        {
            errorNum = -10053;
        }
        catch(CRLException crlex)
        {
            errorNum = -10038;
        }
        catch(ServerKeyStoreException pfxex)
        {
            errorNum = -10047;
        }
        catch(NoSuchAlgorithmException noalgex)
        {
            errorNum = -10043;
        }
        catch(NoSuchProviderException noprex)
        {
            errorNum = -10044;
        }
        catch(Throwable ex)
        {
            log.debug(ex.getMessage());
            errorNum = -10033;
        }
    }

    public byte[] NSSignedAndEncryptedEnvelop(String deSignerDN, String deReciptDN)
    {
        Object obj = ServkeyStoreTable.get(deSignerDN.toLowerCase());
        if(obj == null)
        {
            errorNum = -10028;
            return null;
        }
        ServerKeyStore pfx = (ServerKeyStore)obj;
        obj = encCertTable.get(deReciptDN);
        if(obj == null)
        {
            errorNum = -10031;
            return null;
        }
        X509Certificate cert = (X509Certificate)obj;
        byte tmp1[] = (byte[])null;
        byte tmp[];
        if(plainText == null)
        {
            makePlainText();
            tmp = AttachedSignContent;
        } else
        {
            tmp = plainText;
        }
        if(errorNum != 0)
            return null;
        try
        {
            tmp1 = Base64.encode(netsignapi.MSEnvelopedandSigned(tmp, pfx, cert)).getBytes();
        }
        catch(ServerKeyStoreException pfxex)
        {
            errorNum = -10047;
        }
        catch(InvalidKeyException inkeeyx)
        {
            errorNum = -10042;
        }
        catch(NoSuchProviderException noproex)
        {
            errorNum = -10044;
        }
        catch(NoSuchAlgorithmException nosuaex)
        {
            errorNum = -10043;
        }
        catch(SignatureException sigex)
        {
            errorNum = -10045;
        }
        catch(WriteEnvDataException wrdndateex)
        {
            errorNum = -10049;
        }
        catch(EncryptKeyException exenc)
        {
            errorNum = -10050;
        }
        catch(EncryptDataException encdaex)
        {
            errorNum = -10051;
        }
        catch(EncryptAlgException ealgex)
        {
            errorNum = -10052;
        }
        catch(CertificateException cex)
        {
            errorNum = -10040;
        }
        catch(InvalidCertificateException invlidex)
        {
            errorNum = -10041;
        }
        catch(Throwable ex)
        {
            errorNum = -10034;
            log.debug( "NSSignedAndEncryptedEnvelop error:" + ex.getMessage());
            log.debug("NSSignedAndEncryptedEnvelop error:" + ex.getMessage());
        }
        return tmp1;
    }

    public void NSDecryptedAndVerifiedEnvelop(byte signedAndEnvelopedMsg[], String deReciptDN)
    {
        Object obj = ServkeyStoreTable.get(deReciptDN.toLowerCase());
        if(obj == null)
        {
            errorNum = -10028;
            return;
        }
        ServerKeyStore pfx = (ServerKeyStore)obj;
        try
        {
            netsignapi.MSDecrypedandVerify(Base64.decode(signedAndEnvelopedMsg), pfx, trustCerts, CRLTables);
        }
        catch(CRLException crlex)
        {
            errorNum = -10038;
        }
        catch(CertificateException cerex)
        {
            errorNum = -10040;
        }
        catch(InvalidCertificateException inex)
        {
            errorNum = Integer.parseInt(inex.getMessage());
        }
        catch(InvalidKeyException exkey)
        {
            errorNum = -10042;
        }
        catch(NoSuchProviderException nosuex)
        {
            errorNum = -10044;
        }
        catch(NoSuchAlgorithmException noalg)
        {
            errorNum = -10043;
        }
        catch(SignatureException sigex)
        {
            errorNum = -10045;
        }
        catch(ServerKeyStoreException seke)
        {
            errorNum = -10047;
        }
        catch(DecryptKeyException dece)
        {
            errorNum = -10048;
        }
        catch(Throwable ex)
        {
            errorNum = -10035;         
            log.debug("NSDecryptedAndVerifiedEnvelop error:" + ex.getMessage());
        }
    }

    /**
     * @deprecated Method NetSignSignVerify is deprecated
     */

    public int NetSignSignVerify(String signMsg)
    {
        int tmpInt = -1;
        try
        {
            byte tempB[] = Base64.decode(signMsg);
            netsignapi.VerifySingleSignedMsg(tempB, trustCerts, CRLTables);
            getTotle();
            return 1;
        }
        catch(CertificateException e0)
        {
            return -14;
        }
        catch(CRLException e2)
        {
            return -17;
        }
        catch(InvalidCertificateException e1)
        {
            return tmpInt = -2;
        }
        catch(Throwable e1)
        {
            e1.printStackTrace();
        }
        return tmpInt;
    }

    /**
     * @deprecated Method NetSignDecryptAndVerifyMessage is deprecated
     */

    public int NetSignDecryptAndVerifyMessage(String envelopBuf, String decryptSub)
    {
        int tmpInt = -1;
        String tmpCN = "CN=" + decryptSub.trim() + ",";
        try
        {
            byte tempB[];
            try
            {
                tempB = Base64.decode(envelopBuf);
            }
            catch(IOException ex)
            {
                return -27;
            }
            Enumeration e = ServkeyStoreTable.keys();
            String keyStoreDN = null;
            String tmpp = null;
            while(e.hasMoreElements()) 
            {
                tmpp = (String)e.nextElement();
                log.debug(tmpp);
                if(tmpp.indexOf(tmpCN) != -1)
                {
                    keyStoreDN = tmpp;
                    break;
                }
            }
            if(keyStoreDN == null)
            {
                log.debug("\u8F93\u5165\u7684DN\u65E0\u6548\uFF01");
                return -25;
            }
            Object obj = ServkeyStoreTable.get(keyStoreDN.toLowerCase());
            ServerKeyStore pfx = (ServerKeyStore)obj;
            netsignapi.MSDecrypedandVerify(tempB, pfx, trustCerts, CRLTables);
            getTotle();
            tmpInt = 1;
        }
        catch(DecryptDataException e)
        {
            return -26;
        }
        catch(DecryptKeyException e)
        {
            return -26;
        }
        catch(CertificateException e0)
        {
            return -14;
        }
        catch(InvalidCertificateException e1)
        {
            return tmpInt = -2;
        }
        catch(CertificateNotMatchException e1)
        {
            e1.printStackTrace();
            return -25;
        }
        catch(CRLException e2)
        {
            return -17;
        }
        catch(ServerKeyStoreException e)
        {
            return -25;
        }
        catch(Throwable throwable) { }
        return tmpInt;
    }

    public void NSAttachedVerify(byte signedMsg[], boolean needVerifyCRL)
    {
        if(needVerifyCRL)
        {
            NSAttachedVerify(signedMsg);
            return;
        }
        try
        {
            netsignapi.VerifySingleSignedMsgWithoutCRL(Base64.decode(signedMsg), trustCerts);
        }
        catch(CertificateException ex)
        {
            errorNum = -10040;
        }
        catch(CRLException ex1)
        {
            errorNum = -10038;
        }
        catch(InvalidCertificateException ex2)
        {
            errorNum = Integer.parseInt(ex2.getMessage());
        }
        catch(Throwable ex3)
        {
            ex3.printStackTrace();
            errorNum = -10029;
        }
    }

    /**
     * @deprecated Method NetSignDetachedSignVerify is deprecated
     */

    public int NetSignDetachedSignVerify(String signature, byte plainText[])
    {
        int tmpInt = -1;
        try
        {
            byte tempB[] = Base64.decode(signature);
            netsignapi.VerifySingleSignedMsg(tempB, plainText, trustCerts, CRLTables);
            tmpInt = 1;
        }
        catch(CertificateException e0)
        {
            return -14;
        }
        catch(InvalidCertificateException e1)
        {
            return tmpInt = -2;
        }
        catch(CRLException e2)
        {
            return -17;
        }
        catch(Exception exception) { }
        return tmpInt;
    }

    /**
     * @deprecated Method GetCertIssuer is deprecated
     */

    public String GetCertIssuer()
    {
        return netsignapi.getSignCertInfo(2);
    }

    public static String GetVersion()
    {
        return "Version:1.8.056.1 Build:200709031533";
    }

    /**
     * @deprecated Method GetUsrID is deprecated
     */

    public String GetUsrID()
    {
        return netsignapi.getSignCertInfo(1);
    }

    /**
     * @deprecated Method GetCertSerialNumber is deprecated
     */

    public String GetCertSerialNumber()
    {
        return netsignapi.getSignCertInfo(5);
    }

    /**
     * @deprecated Method GetCertValidTime_start is deprecated
     */

    public String GetCertValidTime_start()
    {
        return netsignapi.getSignCertInfo(3);
    }

    /**
     * @deprecated Method GetCertValidTime_end is deprecated
     */

    public String GetCertValidTime_end()
    {
        return netsignapi.getSignCertInfo(4);
    }

    public byte[][] GetFileInfo()
    {
        return NSGetFileInfo();
    }

    public byte[] GetFormItem()
    {
        return NSGetFormItem();
    }

    byte AttachedSignContent[];
    private static Hashtable ServkeyStoreTable = new Hashtable();
    private static Hashtable encCertTable;
    private static Hashtable Trustconfgtable = new Hashtable();
    private static TrustCerts trustCerts = new TrustCerts();
    private static HashMap CRLTables = new HashMap();
    private static final String crlsAndCertStoreProperties = "netsign";
    public static String logFile;
    private NetSignImpl netsignapi;
    public static final int index_CertSubject = 1;
    public static final int index_CertISSUser = 2;
    public static final int index_CertValidTime_start = 3;
    public static final int index_CertValidTime_end = 4;
    public static final int index_CertSerialnumber = 5;
    public static final int ER_ITEMDATA_ERROR = -10026;
    public static final int ER_FILEDATA_ERROR = -10025;
    public static final int ER_DATA_MISSED = -10006;
    public static final int ER_PARSEDATA_ERROR = -10024;
    public static final int ER_OUTOFLENTH_ERROR = -10027;
    public static final int ER_CERT_SIGNATURE_ERROR = -10010;
    public static final int ER_NOTFOUNDSIGNCERT_ERROR = -10028;
    public static final int ER_VeryAttached_ERROR = -10029;
    public static final int ER_VeryDetached_ERROR = -10030;
    public static final int ER_NOTFOUNDEnCCERT_ERROR = -10031;
    public static final int ER_Enveloped_ERROR = -10032;
    public static final int ER_EnvelopedDecryped_ERROR = -10033;
    public static final int ER_SIGNEANDENCRYPETDENVELOP_ERROR = -10034;
    public static final int ER_SIGNEANDDECRYPTEDENVELOP_ERROR = -10035;
    public static final int ER_CERT_INVALID = -10039;
    public static final int ER_CRL_ERROR = -10038;
    public static final int ER_CERTEXCEPTION = -10040;
    public static final int ER_InvalidCertificateException = -10041;
    public static final int ER_InvalidKeyException = -10042;
    public static final int ER_NoSuchAlgorithmException = -10043;
    public static final int ER_NoSuchProviderException = -10044;
    public static final int ER_SignatureException = -10045;
    public static final int ER_ServerKeyStoreException = -10047;
    public static final int ER_DecryptKeyException = -10048;
    public static final int ER_WriteEnvDataException = -10049;
    public static final int ER_EncryptKeyException = -10050;
    public static final int ER_EncryptDataException = -10051;
    public static final int ER_EncryptAlgException = -10052;
    public static final int ER_DecryptDataException = -10053;
    public static final int ER_ENCODEBASE64_ERROR = -10046;
    public static final int ER_NOINROOTCERTLIST_ERROR = -10054;
    public static final int ER_TIMEOUTANDNOTTRUST_ERROR = -10055;
    public static final int ER_ISREVOKED_ERROR = -10056;
    public static final int ER_NOROOTCERT_ERROR = -10057;
    public static final int ER_VerifyNukedSignMsg = -10060;
    public static final int ER_Decrypt_verifyNukedSignMsg = -10061;
    public static final int ER_RAWSIGN_NO_DATA = -10062;
    public static final int ER_RAWSIGN_ERROR = -10063;
    public static final int ER_BASE64_ENCODE = -10064;
    private byte m_formContents[];
    private byte m_nameOfFiles[];
    private byte plainText[];
    private int totalFormLen;
    private int totalFileLen;
    private int formCounter;
    private int fileCounter;
    private Hashtable fileItems;
    private String formItems[];
    private int errorNum;
    public static boolean isAPIPropertySetted = false;
    public static String lastCRLPortal = null;
    private static boolean crlLoadReady = false;
    private static boolean firstCRLLoadReady = false;

    static 
    {
        Security.addProvider(new InfosecProvider());
    }
}
