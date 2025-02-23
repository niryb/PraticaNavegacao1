package com.example.navegacao1.ui.telas

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.UsuarioDAO
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaCadastro(modifier: Modifier = Modifier, onCadastradoComSucesso: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    var cadastroRealizado by remember { mutableStateOf(false) }  // variável para controle do estado do cadastro

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text(text = "Cadastrar Login") })
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = senha, visualTransformation = PasswordVisualTransformation(),
            onValueChange = { senha = it }, label = { Text(text = "Cadastrar Senha") })

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            scope.launch(Dispatchers.IO) {
                usuarioDAO.adicionar(Usuario(nome = login, senha = senha)) { usuarioAdicionado ->
                    if (usuarioAdicionado != null) {
                        cadastroRealizado = true  // atualiza o estado para indicar que o cadastro foi realizado
                        onCadastradoComSucesso()
                    } else {
                        mensagemErro = "Erro ao cadastrar usuário!"
                    }
                }
            }
        }) {
            Text("Cadastrar")
        }

        if (cadastroRealizado) {
            LaunchedEffect(cadastroRealizado) {
                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                cadastroRealizado = false  // reseta o estado para não exibir o toast novamente
            }
        }

        mensagemErro?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                mensagemErro = null
            }
        }
    }
}


