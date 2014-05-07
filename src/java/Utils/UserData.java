package Utils;

public class UserData {
    String username;
    String email;
    String name;
    String pass;
    String dataNascimento;
    String dataInscricao;
    String pathImagem;
    String tipoConta;
    
    public void setUsername( String value ) {
        username = value;
    }
    
    public void setName(String value) {
        name = value;
    }
    
    public void setPassword(String value) {
        pass = value;
    }
    
    public void setEmail( String value ) {
        email = value;
    }

    public void setDataNascimento(String value) {
       dataNascimento = value;
    }

    public void setDataInscricao(String value) {
        dataInscricao = value;
    }

    public void setPathImagem(String value) {
        pathImagem = value;
    }

    public void setTipoConta(String value) {
        tipoConta = value;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return pass;
    }

    public String getDataInscricao() {
        return dataInscricao;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public String getPathImagem() {
        return pathImagem;
    }
    
    
}