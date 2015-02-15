class Instructor < User

	has_many    :groups, dependent: :destroy
    has_many    :quizzes, dependent: :destroy

    def self.model_name
		User.model_name
	end

end