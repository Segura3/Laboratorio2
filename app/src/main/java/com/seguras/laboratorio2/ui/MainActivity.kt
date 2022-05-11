package com.seguras.laboratorio2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.seguras.laboratorio2.databinding.ActivityMainBinding
import com.seguras.laboratorio2.model.Login
import com.seguras.laboratorio2.services.APIService
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var response: Boolean = false
    private lateinit var user: Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener {
            if(validaCampos()){
                response = userLogin(binding.tietMail.text.toString(), binding.tietPass.text.toString())
                //Log.i("LOG", "Response: "+response.toString())

                if(response){
                    val intent = Intent(this, RestaurantsActivity::class.java)
                    val parametros = Bundle()
                    //Simulacion de usuario especifico en respuesta
                    user.userName = binding.tietName.text.toString()
                    parametros.putSerializable("logUser", user)
                    intent.putExtras(parametros)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnReg.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.tietName.setText("")
        binding.tietMail.setText("")
        binding.tietPass.setText("")
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://demo4802870.mockable.io/usuarios/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun userLogin(email: String, psw: String): Boolean{
        var res: Boolean = false
        //Log.i("LOG", email)
        //Log.i("LOG", psw)

        GlobalScope.launch(Dispatchers.IO) {
            val call: Response<Login> = getRetrofit().create(APIService::class.java).login(validaLogin(email,psw))
            val resu: Login? = call.body()
            if(call.isSuccessful){
                res = resu?.status.toString()=="Success"
                //Log.i("LOG", "Resu: "+resu?.status.toString())
                user = resu!!
            }else{
                showError()
            }
        }

        Thread.sleep(2000)

        //Log.i("LOG", "Res: "+res.toString())
        return res
    }

    private fun validaLogin(email: String,psw: String): String{
        //Simulacion de la validacion del lado servidor
        var result: String = "flogin"
        if(email=="hbarenskie0@purevolume.com" && psw == "1234567a"){
            result = "login"
        }else if(email=="rlamden1@myspace.com" && psw == "1234567b"){
            result = "login"
        }else if(email=="dstarford2@purevolume.com" && psw == "1234567c"){
            result = "login"
        }else if(email=="tstorror3@gravatar.com" && psw == "1234567d"){
            result = "login"
        }else if(email=="lpetronis4@springer.com" && psw == "1234567e"){
            result = "login"
        }
        //Log.i("LOG", result)
        return result
    }

    private fun showError(){
        Toast.makeText(this, "Error en el login", Toast.LENGTH_LONG).show()
    }

    fun validaCampos(): Boolean{

        val pswRegex = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=[^A-Za-z]*[A-Za-z])" + //at least 1 letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 6 characters
                "$")
        var res = true

        with(binding){
            if(tietName.text.toString().isEmpty()){
                tietName.error = "Valor requerido"
                res = false
            }else tietName.error = null
            if(tietMail.text.toString().isEmpty()){
                tietMail.error = "Valor requerido"
                res = false
            }else if(!Patterns.EMAIL_ADDRESS.matcher(tietMail.text.toString()).matches()){
                tietMail.error = "Ingresa un correo valido"
                res = false
            }else tietMail.error = null
            if(tietPass.text.toString().isEmpty()){
                tietPass.error = "Valor requerido"
                res = false
            }else if(!pswRegex.matcher(tietPass.text).matches()){
                tietPass.error = "La contraseña debe tener al menos 8 caracteres que incluyan por lo menos una letra y un numero"
                res = false
            } else tietPass.error = null
        }
        return res
    }
}