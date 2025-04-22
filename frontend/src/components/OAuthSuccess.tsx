import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

export default function OAuthSuccess() {
    const [params] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        const token = params.get('token');
        if(token) {
            localStorage.setItem('authToken', token);
            navigate("/home");
        } else {
            navigate("/");
        }
    }, [params, navigate]);

    return (
        <>Logging in...</>
    )
}