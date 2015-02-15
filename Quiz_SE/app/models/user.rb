class User < ActiveRecord::Base

  before_save :ensure_authentication_token

  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
       :recoverable, :rememberable, :trackable, :validatable,
       :token_authenticatable,:confirmable


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




  #A method used to bypass confirmation step after creating a new userx
  def skip_confirmation!
    self.confirmed_at = Time.now
  end

  def full_name
    first_name + " " + last_name
  end

end
