package l2s.commons.dao;

import java.io.Serializable;

public interface JdbcEntity extends Serializable
{
	void setJdbcState(JdbcEntityState state);

	JdbcEntityState getJdbcState();

	void save();

	void update();

	void delete();

	default void updated(boolean store)
	{
		if(getJdbcState().isPersisted())
		{
			setJdbcState(JdbcEntityState.UPDATED);
			if(store)
			{
				update();
			}
		}
		else if(getJdbcState().isSavable())
		{
			if(store)
			{
				save();
			}
		}
	}
}
