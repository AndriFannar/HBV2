package is.hi.afk6.hbv2.entities.callbacks;

import is.hi.afk6.hbv2.entities.ResponseWrapper;

public interface APICallback<T>
{
    public void onComplete(ResponseWrapper<T> result);
}
