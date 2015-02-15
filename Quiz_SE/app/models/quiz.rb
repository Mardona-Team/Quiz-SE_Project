class Quiz < ActiveRecord::Base

	#Relatioships
	belongs_to :instructor, class_name: 'User', foreign_key: 'instructor_id'
	has_many :publications
	has_many :groups, through: :publicationsup
	has_many :questions
	has_many :students_quizzes
	has_many :students, through: :students_quizzes
	accepts_nested_attributes_for :questions

	#Validations
	validates_presence_of :title
	validates_presence_of :subject
	validates_presence_of :year
	validates :marks, presence: true, :numericality => { less_than_or_equal_to: 10000, greater_than: 0 }

    def set_published
    	self.status = true
    end

	def published
		self.groups.any?
	end

    def published_on(group)
    	if published_on?(group)
    		"1"
    	else
    		"0"
    	end
    end

	def published_on?(group)
		group.quizzes.include? self
	end

	def show_full_details(options={})
		as_json(:only => [:id, :title, :subject, :year, :description, :marks, :created_at],
					:include => {
						:questions => {:only => [:id, :title],
							:methods => [:shuffled_answers]
						},
					})
	end
end
