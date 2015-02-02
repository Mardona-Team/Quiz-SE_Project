class Quiz < ActiveRecord::Base
	
	#Relatioships
	belongs_to :instructor, class_name: 'User', foreign_key: 'instructor_id'
	belongs_to :group
	has_many :questions

	#Validations
	validates_presence_of :title
	validates_presence_of :subject
	validates_presence_of :year
	validates :marks, presence: true, less_than_or_equal_to: 10000, greater_than: 0

	def show_full_details(options={})
		super(:only => [:id, :title, :subject, :year, :description, :marks, :created_ar],
				:include => {
					:questions => {:only => [:id, :title],
			  			:methods => [:shuffled_answers]
			  		},
				}
			)
	end
end
