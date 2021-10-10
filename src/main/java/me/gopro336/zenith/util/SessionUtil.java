package me.gopro336.zenith.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.lang.reflect.Field;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionUtil {
    public static Session createSession(String username, String password, Proxy proxy) throws AuthenticationException {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(proxy, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)((Object)service.createUserAuthentication(Agent.MINECRAFT));
        auth.setUsername(username);
        auth.setPassword(password);
        auth.logIn();
        return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
    }

    public static boolean login(String email, String password) {
        try {
            Session session = SessionUtil.createSession(email, password, Proxy.NO_PROXY);
            Field field = Minecraft.class.getDeclaredField("session");
            field.setAccessible(true);
            field.set(Minecraft.getMinecraft(), session);
            return true;
        }
        catch (Exception ignored) {
            return false;
        }
    }

    public static Session getSession() {
        return Minecraft.getMinecraft().getSession();
    }
}
