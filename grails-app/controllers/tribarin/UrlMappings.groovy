package tribarin

class UrlMappings {

    static mappings = {
        "/api/login/$action?/$id?"(controller: 'login')

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'application', action:'index')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

