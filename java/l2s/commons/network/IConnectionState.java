package l2s.commons.network;

import io.netty.util.AttributeKey;

/**
 * @author Nos
 */
public interface IConnectionState
{
	AttributeKey<IConnectionState> CONNECTION_STATE_ATTRIBUTE_KEY = AttributeKey.valueOf(IConnectionState.class, "");
}
