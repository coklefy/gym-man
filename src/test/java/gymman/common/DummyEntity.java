package gymman.common;

public class DummyEntity extends BaseEntity {
	private String name;

	private DummyEntity() {
	}

	public String getName() {
		return this.name;
	}

	public static class Builder {
		private String id;
		private String name = "";

		public Builder() { }

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public DummyEntity build() {
			DummyEntity e = new DummyEntity();

			if (this.id != null) {
				e.id = this.id;
			}

			e.name = this.name;

			return e;
		}
	}
}
