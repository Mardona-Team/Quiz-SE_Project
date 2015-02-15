module API


  class RegistrationsController < Devise::RegistrationsController
    skip_before_filter :verify_authenticity_token,
    :if => Proc.new { |c| c.request.format == 'application/json' }

    respond_to :json

    def create
      build_resource(sign_up_params)
      resource.skip_confirmation!
      if resource.save
        sign_in resource
        render :status => 200,
        :json => { :success => true,
          :info => "Registered",
          :data => { :user => resource,
           :auth_token => current_user.authentication_token,
           :id => current_user.id,
           :first_name => current_user.first_name,
           :last_name => current_user.last_name } }
         else
          render :status => :unprocessable_entity,
          :json => { :success => false,
            :info => resource.errors,
            :data => {} }
          end
        end

        def sign_up_params
          params.require(:user).permit(:username, :first_name, :last_name, :password, :email, :type, :faculty, :university, :department, :year)
        end
      end
    end