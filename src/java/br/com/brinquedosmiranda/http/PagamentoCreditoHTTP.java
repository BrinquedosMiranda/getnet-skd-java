/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.brinquedosmiranda.http;

import br.com.brinquedosmiranda.enuns.AmbienteEnum;
import br.com.brinquedosmiranda.enuns.BrandEnum;
import br.com.brinquedosmiranda.enuns.DocumentEnum;
import br.com.brinquedosmiranda.enuns.TransactionEnum;
import br.com.brinquedosmiranda.modelo.Address;
import br.com.brinquedosmiranda.modelo.Billing;
import br.com.brinquedosmiranda.modelo.Card;
import br.com.brinquedosmiranda.modelo.Credit;
import br.com.brinquedosmiranda.modelo.Customer;
import br.com.brinquedosmiranda.modelo.Device;
import br.com.brinquedosmiranda.modelo.GetNet;
import br.com.brinquedosmiranda.modelo.Order;
import br.com.brinquedosmiranda.modelo.Shippings;
import br.com.brinquedosmiranda.modelo.Tokenizacao;
import br.com.brinquedosmiranda.modelo.Verificacao;
import br.com.brinquedosmiranda.util.Credenciar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author conta
 */
public class PagamentoCreditoHTTP {
    //----------------Requisição contendo os dados para pagamento com cartão de crédito--
    private AmbienteEnum ambiente;
    private String clientID;
    private String clientSecret;
    private String setSeller_id;
    private int Amount;
    //----------------Requisição contendo os dados para pagamento com cartão de crédito--
    
    //----------------order----------------------------
     private String order_id;
    //----------------order----------------------------
    
    //----------------customer-------------------------
    private String customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private DocumentEnum documentEnun;
    private String document_number;
    private String phone_number;
    //----------------customer-------------------------
    
    //----------------billing--------------------------
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postal_code;
    //----------------billing--------------------------
    
     //----------------device---------------------------
     private String sessao;
    //----------------device---------------------------
    
    //----------------shippings------------------------
     private String phone_number_shipping;
    //----------------shippings------------------------
     
    //----------------addres------------------------
    private String street_address;
    private String number_address;
    private String complement_address;
    private String districtt_address;
    private String city_address;
    private String state_address;
    private String postal_code_address;
    //----------------addres------------------------
    
    //----------------credit------------------------
    private TransactionEnum transaction;
    private int Number_installments;
    private String soft_descriptor;
    //----------------credit------------------------
    
    //----------------card--------------------------
    private String cardNumber;
    private BrandEnum brand;
    private String cardholder_name;
    private String expiration_month;
    private String expiration_year;
    private String security_code;
    //----------------card--------------------------

  
    
      public PagamentoCreditoHTTP(AmbienteEnum ambiente, String clientID, String clientSecret, String setSeller_id, int Amount, String order_id, String customer_id, String first_name, String last_name, String email, DocumentEnum documentEnun,
              String document_number, String phone_number, String street, String number, String complement, String district,
              String city, String state, String postal_code, String sessao, String phone_number_shipping,
              String street_address, String number_address, String complement_address, String districtt_address,
              String city_address, String state_address, String postal_code_address, TransactionEnum transaction, 
              int Number_installments, String soft_descriptor, String cardNumber, BrandEnum brand, String cardholder_name, 
              String expiration_month, String expiration_year, String security_code) {

        
       //String number_token = null;
        try {
            String urlParameters;
            //------------------------------------------------------------------
            AutenticacaoServiceHTTP AutenticacaoService = new AutenticacaoServiceHTTP();
                String numbertoken = AutenticacaoService.criarAutenticacao(Credenciar.ambiente, Credenciar.ClientID, Credenciar.ClientSecret, cardNumber);
            String access_token = AutenticacaoService.Autenticacao(ambiente, clientID, clientSecret);
            //------------------------------------------------------------------
            String url = ambiente.getUrl() + "/v1/payments/credit";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Authorization", "Bearer " + access_token);
            //----------------parameters-----------------------
            Gson gson = new Gson();
            GetNet getNet = new GetNet();
            Type TypePagamentoCredito = new TypeToken<GetNet>() {
            }.getType();
            //------------------------------------------------------ 
            getNet.setSeller_id(setSeller_id);
            getNet.setAmount(Amount);
            getNet.setCurrency("BRL");
            Order order = new Order();
            order.setOrder_id(order_id);
            getNet.setOrder(order);
            //------------------------------------------------------  
            Customer cus = new Customer();
            cus.setCustomer_id(customer_id);
            cus.setFirst_name(first_name);//Antifraude
            cus.setLast_name(last_name);//Antifraude
            cus.setEmail(email);//Antifraude
            cus.setDocument_type(documentEnun.getDocument());//Antifraude
            cus.setDocument_number(document_number);//Antifraude
            cus.setPhone_number(phone_number);//Antifraude
            Billing bil = new Billing();
            bil.setStreet(street);//Antifraude
            bil.setNumber(number);//Antifraude
            bil.setComplement(complement);//Antifraude
            bil.setDistrict(district);
            bil.setCity(city);//Antifraude
            bil.setState(state);//Antifraude
            bil.setCountry("Brasil");//Antifraude
            bil.setPostal_code(postal_code);//Antifraude
            cus.setBilling_address(bil);
            getNet.setCustomer(cus);
            //------------------------------------------------------ 
            String ipDaMaquina = InetAddress.getLocalHost().getHostAddress();
            Device dev = new Device();
            dev.setIp_address(ipDaMaquina);//Antifraude
            dev.setDevice_id(sessao);//Antifraude
            getNet.setDevice(dev);
            //------------------------------------------------------       
            List<Shippings> listarShipping = new ArrayList<Shippings>();
            Shippings sh = new Shippings();
            sh.setPhone_number(phone_number_shipping);
            listarShipping.add(sh);
            Address address = new Address();
            address.setStreet(street_address);
            address.setNumber(number_address);
            address.setComplement(complement_address);
            address.setDistrict(districtt_address);
            address.setCity(city_address);
            address.setState(state_address);
            address.setCountry("Brasil");
            address.setPostal_code(postal_code_address);
            sh.setAddress(address);
            getNet.setShippings((ArrayList<Shippings>) listarShipping);
            //------------------------------------------------------ 
            Credit credit = new Credit();
            credit.setDelayed(false);
            //credit.setAuthenticated(false);
            //credit.setPre_authorization(false);
            credit.setSave_card_data(false);
            credit.setTransaction_type(transaction.getTransaction());
            credit.setNumber_installments(Number_installments);
            credit.setSoft_descriptor(soft_descriptor);
            Card card = new Card();
            card.setNumber_token(numbertoken);
            card.setCardholder_name(cardholder_name);//Antifraude
            card.setSecurity_code(security_code);
            card.setBrand(brand.getBrand());
            card.setExpiration_month(expiration_month);
            card.setExpiration_year(expiration_year);
            credit.setCard(card);
            getNet.setCredit(credit);
            urlParameters = gson.toJson(getNet, TypePagamentoCredito);
            //----------------parameters-----------------------
            //----------------parameters-----------------------
            System.out.println("verificar" + urlParameters);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

        } catch (Exception erro) {

        }

    }

   

}
