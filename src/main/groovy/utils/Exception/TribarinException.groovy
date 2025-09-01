package utils.exception

class TribarinException extends Exception {
    int codigo
    boolean status
    String allError

    TribarinException(String mensagem, int codigo) {
        super(mensagem)
        this.codigo = codigo
        this.status = false
        this.allError = gerarStackTrace(this)
    }

    private static String gerarStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        throwable.printStackTrace(pw)
        return sw.toString()
    }

    String getAllError() {
        return "Erro completo:\n${this.allError}";
    }

    String getOrigem() {
        return "TribarinException: ${message} - (Código: ${codigo}) (Status: ${status})";
    }

    String getMensagem() {
        return message;
    }

    String getCodigo() {
        return codigo;
    }
}
