package com.seguras.laboratorio2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.seguras.laboratorio2.databinding.ActivityRegistroBinding
import com.seguras.laboratorio2.model.Login
import com.seguras.laboratorio2.services.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    var response: Boolean = false
    private lateinit var nuser: Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCReg.setOnClickListener {
            if(validaCampos()){
                response = userRegister(binding.tietMail.text.toString(), binding.tietPass.text.toString())

                if(response){
                    val intent = Intent(this, RestaurantsActivity::class.java)
                    val parametros = Bundle()
                    //Simulacion de usuario especifico en respuesta
                    nuser.userName = binding.tietName.text.toString()
                    parametros.putSerializable("logUser", nuser)
                    intent.putExtras(parametros)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Favor de volver a intentar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun userRegister(email: String, psw: String): Boolean {
        var res: Boolean = false
        GlobalScope.launch(Dispatchers.IO) {
            val call: Response<Login> = getRetrofit().create(APIService::class.java).login("register")
            val resu: Login? = call.body()
            if(call.isSuccessful){
                res = resu?.status.toString()=="Success"
                nuser = resu!!
            }else{
                showError()
            }
        }
        Thread.sleep(2000)

        return res
    }

    private fun showError(){
        Toast.makeText(this, "Error en el registro", Toast.LENGTH_LONG).show()
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
                tietPass.error = "La contraseña debe tener al menos 8 caracteres que incluiyan por lo menos una letra y un numero"
                res = false
            } else tietPass.error = null
        }
        return res
    }
}