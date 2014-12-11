class User < ActiveRecord::Base



    USERNAME_REGEX = /[a-zA-Z](([\._\-][a-zA-Z0-9])|[a-zA-Z0-9])*[a-z0-9]/
    NAME_REGEX = /[a-zA-Z]/
	
	validates :username, 
	presence: true,
	length: { minimum: 5, maximum: 25},
	uniqueness: true,
	format: USERNAME_REGEX


	validates :first_name, presence: true,format: NAME_REGEX
	validates :last_name, presence: true,format: NAME_REGEX
	validates :type, presence: true




#devise for password and email
   

end



class Student < User

    has_many    :memberships ,foreign_key: 'student_id'


end


class Instructor < User

	has_many    :groups, dependent: :destroy
    has_many    :quizzes, dependent: :destroy


end
